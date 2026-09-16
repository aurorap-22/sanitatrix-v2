import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export default function PazienteDashboard(){
    const nav = useNavigate();
    const [paziente,setPaziente]=useState(null);
    const [prenotazioni,setPrenotazioni]=useState([]);
    const [medici,setMedici]=useState([]);
    const [medicoId,setMedicoId]=useState("");
    const [dataOra,setDataOra]=useState("");
    const [errMsg,setErrMsg]=useState("");
    const [successMsg,setSuccessMsg]=useState("");

    const pazienteId = localStorage.getItem("pazienteId");

    const logout = () => {
        localStorage.clear();
        nav("/login");
    };

    useEffect(()=>{
        if(!pazienteId) return;
        api.get(`/pazienti/${pazienteId}`)
            .then(r=>{
                setPaziente(r.data);
                return api.get(`/prenotazioni/paziente/${r.data.id}`);
            })
            .then(r=> setPrenotazioni(Array.isArray(r.data)? r.data: []))
            .catch(err=> console.error(err));

        api.get("/medici").then(r=> setMedici(r.data)).catch(err=>console.error(err));
    },[pazienteId]);

    const validateDataOra = (value) => {
        if(!value) return null;
        const dt = new Date(value);
        if(isNaN(dt.getTime())) return "Data/ora non valida";
        const day = dt.getDay();
        if(day === 0 || day === 6) return "Le prenotazioni sono consentite solo dal lunedì al venerdì";
        const h = dt.getHours();
        const m = dt.getMinutes();
        const mins = h*60 + m;
        if(mins < 9*60 || mins > 19*60) return "L'orario deve essere compreso tra le 09:00 e le 19:00";
        if(m % 30 !== 0) return "Sono accettati solo orari con intervalli di 30 minuti (es. 09:00, 09:30...)";
        const now = new Date();
        if(dt.getTime() <= now.getTime()) return "Seleziona una data/ora futura";
        return null;
    };

    const handlePrenota = async (e) => {
        e.preventDefault();
        setErrMsg("");
        setSuccessMsg("");
        console.log("[Prenota] submit clicked, medicoId=", medicoId, "dataOra=", dataOra);

        if(!medicoId){
            setErrMsg("Seleziona un medico");
            return;
        }
        if(!dataOra){
            setErrMsg("Seleziona data e ora");
            return;
        }

        const validation = validateDataOra(dataOra);
        if(validation){
            setErrMsg(validation);
            return;
        }

        const payload = {
            paziente: { id: paziente.id },
            medico: { id: Number(medicoId) },
            dataOra: dataOra,
            stato: "PRENOTATA"
        };
        console.log("[Prenota] invio payload:", payload);
        try{
            const res = await api.post("/prenotazioni", payload);
            console.log("[Prenota] OK, risposta:", res.data);
            setPrenotazioni([...prenotazioni, res.data]);
            setMedicoId("");
            setDataOra("");
            setSuccessMsg("Prenotazione effettuata con successo!");
            setTimeout(()=>setSuccessMsg(""),3000);
        } catch(err){
            console.error("[Prenota] ERRORE:", err?.response?.status, err?.response?.data);
            const msg = err.response?.data?.message || err.response?.data?.error || (typeof err.response?.data === "string" ? err.response.data : null) || "Errore durante la prenotazione";
            setErrMsg(typeof msg === "string" ? msg : "Errore durante la prenotazione");
        }
    };

    const oraValidationMsg = validateDataOra(dataOra);

    if(!pazienteId) return <div style={{padding:30}}>Nessun paziente loggato, <a onClick={()=>nav("/login")} style={{color:"#3D2A6B",cursor:"pointer",fontWeight:"bold"}}>torna al login</a></div>;
    if(!paziente) return <div style={{background:'#F3F4F6', minHeight:'100vh', padding:30}}>Caricamento paziente {pazienteId}...</div>;

    return(
        <div style={{ background:'#F3F4F6', minHeight:'100vh', padding:30, fontFamily:'sans-serif' }}>
            <div style={{ maxWidth:1100, margin:'0 auto' }}>
                <div style={{ display:"flex", justifyContent:"space-between", alignItems:"center", marginBottom:20 }}>
                    <div style={{ background:'#E9E0FF', borderRadius:28, padding:'26px 30px', textAlign:'center', flex:1, marginRight:12 }}>
                        <h1 style={{ fontSize:32, fontWeight:900, color:'#3D2A6B', margin:0 }}>Area Paziente</h1>
                        <p style={{ color:'#7A6AA8', marginTop:6 }}>Ciao {paziente.nome} {paziente.cognome} - ID {paziente.id}</p>
                    </div>
                    <button onClick={logout} style={{background:"#fff",border:"1px solid #EDEAF6",color:"#3D2A6B",padding:"12px 20px",borderRadius:14,fontWeight:700,cursor:"pointer"}}>
                        Esci
                    </button>
                </div>
                <div style={{ background:'#D6E8FF', borderRadius:28, padding:28, marginBottom:20 }}>
                    <h2 style={{ fontWeight:800, color:'#1E3A5F', fontSize:22 }}>Prenota una visita</h2>
                    <div style={{fontSize:13, color:'#5A8AC7', marginTop:6}}>
                        Prenotazioni dal lunedì al venerdì, orari 09:00 – 19:00. La prestazione viene scelta automaticamente in base alla specializzazione del medico.
                    </div>
                    {successMsg && <div style={{background:"#D6F5E0",color:"#2A6B4A",borderRadius:12,padding:10,fontSize:13,marginTop:12}}>{successMsg}</div>}
                    {errMsg && <div style={{background:"#FFE1E1",color:"#9A1C1C",borderRadius:12,padding:10,fontSize:13,marginTop:12}}>{errMsg}</div>}
                    {oraValidationMsg && !errMsg && <div style={{background:"#FFF4D6",color:"#7A5A00",borderRadius:12,padding:10,fontSize:13,marginTop:12}}>{oraValidationMsg}</div>}
                    <form noValidate onSubmit={handlePrenota} style={{ display:'flex', gap:12, marginTop:16, flexWrap:'wrap', alignItems:'center' }}>
                        <select value={medicoId} onChange={e=>setMedicoId(e.target.value)} required style={{ flex:1, padding:14, borderRadius:14, border:'2px solid #BFD5F5', minWidth:200, fontSize:15, background:'white', cursor:'pointer' }}>
                            <option value="">Scegli il medico</option>
                            {medici.map(m=><option key={m.id} value={m.id}>{m.nome} {m.cognome} {m.specializzazione?`(${m.specializzazione})`:""}</option>)}
                        </select>
                        <input
                            type="datetime-local"
                            value={dataOra}
                            onChange={e=>setDataOra(e.target.value)}
                            min={new Date().toISOString().slice(0,10)+"T09:00"}
                            step="1800"
                            required
                            style={{ padding:14, borderRadius:14, border:'2px solid #BFD5F5', minWidth:270, fontSize:15, background:'white' }}
                        />
                        <button
                            type="submit"
                            style={{ background:'#3D2A6B', color:'white', border:0, padding:'14px 26px', borderRadius:14, fontWeight:800, cursor:'pointer', fontSize:15, boxShadow:'0 4px 14px rgba(61,42,107,0.25)', transition:'transform 0.08s ease' }}
                            onMouseDown={e => e.currentTarget.style.transform = 'scale(0.97)'}
                            onMouseUp={e => e.currentTarget.style.transform = 'scale(1)'}
                            onMouseLeave={e => e.currentTarget.style.transform = 'scale(1)'}
                            onClick={() => console.log("[Prenota] button physically clicked")}
                        >
                            Prenota
                        </button>
                    </form>
                </div>
                <div style={{ background:'#D6E8FF', borderRadius:28, padding:28 }}>
                    <h2 style={{ fontWeight:800, color:'#1E3A5F', fontSize:22 }}>Le tue prenotazioni ({prenotazioni.length})</h2>
                    {prenotazioni.length === 0 ? (
                        <div style={{marginTop:10,color:"#5A8AC7"}}>Nessuna prenotazione ancora</div>
                    ) : prenotazioni.map(p=>(
                        <div key={p.id} style={{ background:'white', borderRadius:16, padding:14, marginTop:12, display:'flex', justifyContent:'space-between' }}>
                            <div>
                                <div style={{ fontWeight:800, color:'#1E3A5F' }}>Dr. {p.medico?.nome} {p.medico?.cognome} - {p.prestazione?.nome}</div>
                                <div style={{ fontSize:12, color:'#5A8AC7' }}>{p.dataOra?.replace('T',' ').slice(0,16)}</div>
                            </div>
                            <div style={{ background:'#D6F5E0', borderRadius:20, padding:'6px 14px', fontWeight:700, color:'#2A6B4A', height:'fit-content' }}>{p.stato}</div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    )
}