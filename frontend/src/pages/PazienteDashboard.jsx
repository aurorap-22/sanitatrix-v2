import { useEffect, useState, useMemo } from "react";
import axios from "axios";

export default function PazienteDashboard() {
    const [prenotazioni, setPrenotazioni] = useState([]);
    const [referti, setReferti] = useState([]);
    const [medici, setMedici] = useState([
        {id:1, nome:"Fabio", cognome:"Volo", specializzazione:"CARDIOLOGIA"},
        {id:2, nome:"Mario", cognome:"Rossi", specializzazione:"OCULISTICA"}
    ]);
    const [prestazioni, setPrestazioni] = useState([
        {id:1, nome:"Visita Base", tipo:"VISITA_BASE"},
        {id:2, nome:"Visita Cardiologica", tipo:"CARDIOLOGIA"},
        {id:3, nome:"Visita Oculistica", tipo:"OCULISTICA"},
        {id:4, nome:"Visita Dermatologica", tipo:"DERMATOLOGIA"},
    ]);
    const [medicoId, setMedicoId] = useState("");
    const [prestazioneId, setPrestazioneId] = useState("");
    const [dataGiorno, setDataGiorno] = useState("");
    const [orario, setOrario] = useState("");
    const [slotLiberi, setSlotLiberi] = useState([]);

    useEffect(()=>{
        const carica = async ()=>{
            try{
                const token = localStorage.getItem("token") || localStorage.getItem("jwt");
                const headers = token? {Authorization:`Bearer ${token}`} : {};

                const r3 = await axios.get("http://localhost:8080/api/medici", {headers});
                const lista = Array.isArray(r3.data)? r3.data : r3.data.content || [];
                if(lista.length>0) setMedici([...new Map(lista.map(m=>[m.id,m])).values()]);

                try{
                    const rP = await axios.get("http://localhost:8080/api/prestazioni", {headers});
                    if(rP.data?.length>0) setPrestazioni(rP.data);
                }catch{}

                try{
                    const r1 = await axios.get("http://localhost:8080/api/prenotazioni/mie", {headers});
                    setPrenotazioni(r1.data || []);
                }catch{
                    const r1b = await axios.get("http://localhost:8080/api/prenotazioni", {headers});
                    setPrenotazioni(r1b.data || []);
                }

                try{
                    const r2 = await axios.get("http://localhost:8080/api/referti/miei", {headers});
                    setReferti(r2.data || []);
                }catch{
                    const r2b = await axios.get("http://localhost:8080/api/referti", {headers});
                    setReferti(r2b.data || []);
                }
            }catch(e){ console.log("uso finti", e) }
        };
        carica();
    },[]);

    const medicoSelezionato = useMemo(() => medici.find(m => String(m.id) === String(medicoId)), [medici, medicoId]);

    const prestazioniFiltrate = useMemo(() => {
        if (!medicoSelezionato) return [];
        const spec = medicoSelezionato.specializzazione;
        return prestazioni.filter(p => p.tipo === "VISITA_BASE" || p.tipo === spec || p.nome?.toUpperCase().includes(spec));
    }, [prestazioni, medicoSelezionato]);

    useEffect(()=>{
        const caricaSlot = async () => {
            if (!medicoId ||!dataGiorno) { setSlotLiberi([]); return; }
            try {
                const token = localStorage.getItem("token") || localStorage.getItem("jwt");
                const headers = token? {Authorization:`Bearer ${token}`} : {};
                const res = await axios.get(`http://localhost:8080/api/prenotazioni/slot?medicoId=${medicoId}&data=${dataGiorno}`, {headers});
                setSlotLiberi(res.data);
            } catch(e){ console.log(e); setSlotLiberi([]); }
        };
        caricaSlot();
        setOrario("");
    }, [medicoId, dataGiorno]);

    useEffect(()=>{ setPrestazioneId(""); }, [medicoId]);

    const handlePrenota = async (e)=>{
        e.preventDefault();
        if(!medicoId ||!prestazioneId ||!dataGiorno ||!orario) return alert("Compila tutto");
        const dataOra = `${dataGiorno}T${orario}:00`;
        try{
            const token = localStorage.getItem("token") || localStorage.getItem("jwt");
            const headers = token? {Authorization:`Bearer ${token}`} : {};
            await axios.post("http://localhost:8080/api/prenotazioni", {
                medico: {id: Number(medicoId)},
                prestazione: {id: Number(prestazioneId)},
                dataOra: dataOra
            }, {headers});
            alert(`Prenotato per ${dataGiorno} alle ${orario}!`);
            window.location.reload();
        }catch(err){
            alert(err.response?.data?.message || err.response?.data || err.message);
        }
    };

    return (
        <div style={s.page}>
            <h1 style={s.h1}>La tua area personale</h1>

            <div style={s.grid}>
                <div style={s.card}>
                    <div style={s.badge}>Nuova prenotazione</div>
                    <form onSubmit={handlePrenota} style={s.form}>
                        <label style={s.label}>Medico</label>
                        <select style={s.input} value={medicoId} onChange={e=>setMedicoId(e.target.value)}>
                            <option value="">Seleziona medico</option>
                            {medici.map(m=>(<option key={m.id} value={m.id}>{m.nome} {m.cognome} - {m.specializzazione}</option>))}
                        </select>

                        <label style={s.label}>Prestazione</label>
                        <select style={s.input} value={prestazioneId} onChange={e=>setPrestazioneId(e.target.value)} disabled={!medicoSelezionato}>
                            <option value="">{!medicoSelezionato? "Prima scegli un medico" : "Seleziona prestazione"}</option>
                            {prestazioniFiltrate.map(p=>(<option key={p.id} value={p.id}>{p.nome}</option>))}
                        </select>

                        <label style={s.label}>Giorno (lun-ven)</label>
                        <input style={s.input} type="date" value={dataGiorno} onChange={e=>setDataGiorno(e.target.value)} />

                        <label style={s.label}>Orario (slot 30 min)</label>
                        <select style={s.input} value={orario} onChange={e=>setOrario(e.target.value)} disabled={slotLiberi.length===0}>
                            <option value="">{slotLiberi.length===0? "Scegli giorno e medico" : "Seleziona orario"}</option>
                            {slotLiberi.map(h=><option key={h} value={h}>{h}</option>)}
                        </select>

                        <button style={s.btn}>Prenota ora</button>
                    </form>
                </div>

                <div style={{display:"flex", flexDirection:"column", gap:"20px"}}>
                    <div style={s.card}>
                        <h3 style={s.cardTitle}>Prenotazioni</h3>
                        {prenotazioni.length===0? <p style={s.empty}>Nessuna prenotazione</p> : prenotazioni.map(p=>(
                            <div key={p.id} style={s.row}>
                                <b>{p.dataOra?.replace("T"," ")?.substring(0,16)}</b> - {p.medico?.nome} {p.stato}
                            </div>
                        ))}
                    </div>

                    <div style={s.card}>
                        <h3 style={s.cardTitle}>Storico Referti</h3>
                        {referti.length===0? <p style={s.empty}>Nessun referto disponibile</p> : referti.map(r=>(
                            <div key={r.id} style={s.row}>
                                <b>{r.data?.substring(0,10) || ""}</b> - {r.descrizione || r.titolo || "Referto"}
                                <a href={r.fileUrl} target="_blank" style={{marginLeft:"8px", color:"#7a6fbf"}}>Apri</a>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

const s={
    page:{minHeight:"100vh", background:"#f3f4f8", padding:"28px", fontFamily:"Inter, sans-serif"},
    h1:{color:"#7a6fbf", fontWeight:800, margin:0, fontSize:"28px", marginBottom:"16px"},
    grid:{display:"grid", gridTemplateColumns:"380px 1fr", gap:"20px"},
    card:{background:"white", borderRadius:"20px", padding:"20px", border:"1px solid #ecebf6", boxShadow:"0 8px 24px rgba(184,174,230,0.15)"},
    badge:{background:"#e9e6f9", color:"#7a6fbf", padding:"6px 12px", borderRadius:"20px", fontSize:"12px", fontWeight:700, display:"inline-block", marginBottom:"12px"},
    label:{fontSize:"11px", fontWeight:600, color:"#8a86a3", marginTop:"8px"},
    input:{padding:"12px", borderRadius:"12px", border:"1px solid #e6e3f3", background:"#fafafd", fontSize:"14px"},
    btn:{marginTop:"14px", padding:"12px", borderRadius:"12px", border:"none", background:"#b8aee6", color:"white", fontWeight:700, cursor:"pointer"},
    cardTitle:{color:"#7a6fbf", margin:"0 0 10px 0", fontSize:"16px"},
    empty:{color:"#b0aacb", fontSize:"13px"},
    form:{display:"flex", flexDirection:"column", gap:"6px"},
    row:{padding:"8px 0", borderBottom:"1px solid #f0eef8", fontSize:"13px", color:"#5a5670"}
}