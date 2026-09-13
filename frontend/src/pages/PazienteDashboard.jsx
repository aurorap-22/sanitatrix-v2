import { useEffect, useState } from "react";
import axios from "axios";

export default function PazienteDashboard(){
    const [paziente,setPaziente]=useState(null);
    const [prenotazioni,setPrenotazioni]=useState([]);
    const [medici,setMedici]=useState([]);
    const [prestazioni,setPrestazioni]=useState([]);
    const [medicoId,setMedicoId]=useState("");
    const [prestazioneId,setPrestazioneId]=useState("");
    const [dataOra,setDataOra]=useState("");

    const utente = JSON.parse(localStorage.getItem("utente"));

    useEffect(()=>{
        if(!utente?.id) return;
        axios.get(`http://localhost:8080/api/pazienti/utente/${utente.id}`)
            .then(r=>{
                setPaziente(r.data);
                return axios.get(`http://localhost:8080/api/prenotazioni/paziente/${r.data.id}`);
            })
            .then(r=> setPrenotazioni(Array.isArray(r.data)? r.data: []))
            .catch(()=>{});

        axios.get("http://localhost:8080/api/medici").then(r=> setMedici(r.data));
        axios.get("http://localhost:8080/api/prestazioni").then(r=> setPrestazioni(r.data));
    },[]);

    const handlePrenota = async (e) => {
        e.preventDefault();
        const payload = {
            paziente: { id: paziente.id },
            medico: { id: Number(medicoId) },
            prestazione: { id: Number(prestazioneId) },
            dataOra: dataOra,
            stato: "PRENOTATA"
        };
        try{
            const res = await axios.post("http://localhost:8080/api/prenotazioni", payload);
            setPrenotazioni([...prenotazioni, res.data]);
        } catch(err){
            alert(err.response?.data?.message || "Errore");
        }
    };

    if(!paziente) return <div style={{background:'#F3F4F6', minHeight:'100vh', padding:30}}>Caricamento...</div>;

    return(
        <div style={{ background:'#F3F4F6', minHeight:'100vh', padding:30, fontFamily:'sans-serif' }}>
            <div style={{ maxWidth:1100, margin:'0 auto' }}>

                <div style={{ background:'#E9E0FF', borderRadius:28, padding:'26px 30px', marginBottom:20, textAlign:'center' }}>
                    <h1 style={{ fontSize:32, fontWeight:900, color:'#3D2A6B', margin:0 }}>Area Paziente</h1>
                    <p style={{ color:'#7A6AA8', marginTop:6 }}>Ciao {paziente.nome} {paziente.cognome} - ID {paziente.id}</p>
                </div>

                <div style={{ background:'#D6E8FF', borderRadius:28, padding:28, marginBottom:20 }}>
                    <h2 style={{ fontWeight:800, color:'#1E3A5F', fontSize:22 }}>Prenota una visita</h2>
                    <form onSubmit={handlePrenota} style={{ display:'flex', gap:12, marginTop:16, flexWrap:'wrap' }}>
                        <select value={medicoId} onChange={e=>setMedicoId(e.target.value)} required style={{ flex:1, padding:12, borderRadius:12, border:0 }}>
                            <option value="">Medico</option>
                            {medici.map(m=><option key={m.id} value={m.id}>{m.nome} {m.cognome}</option>)}
                        </select>
                        <select value={prestazioneId} onChange={e=>setPrestazioneId(e.target.value)} required style={{ flex:1, padding:12, borderRadius:12, border:0 }}>
                            <option value="">Prestazione</option>
                            {prestazioni.map(p=><option key={p.id} value={p.id}>{p.nome}</option>)}
                        </select>
                        <input type="datetime-local" value={dataOra} onChange={e=>setDataOra(e.target.value)} required style={{ padding:12, borderRadius:12, border:0 }}/>
                        <button type="submit" style={{ background:'#3D2A6B', color:'white', border:0, padding:'12px 22px', borderRadius:12, fontWeight:800, cursor:'pointer' }}>Prenota</button>
                    </form>
                </div>

                <div style={{ background:'#D6E8FF', borderRadius:28, padding:28 }}>
                    <h2 style={{ fontWeight:800, color:'#1E3A5F', fontSize:22 }}>Le tue prenotazioni ({prenotazioni.length})</h2>
                    {prenotazioni.length===0 ? <p style={{ color:'#5A8AC7', marginTop:12 }}>Nessuna prenotazione</p> :
                        prenotazioni.map(p=>(
                            <div key={p.id} style={{ background:'white', borderRadius:16, padding:14, marginTop:12, display:'flex', justifyContent:'space-between' }}>
                                <div>
                                    <div style={{ fontWeight:800, color:'#1E3A5F' }}>Dr. {p.medico?.cognome} - {p.prestazione?.nome}</div>
                                    <div style={{ fontSize:12, color:'#5A8AC7' }}>{p.dataOra?.replace('T',' ').slice(0,16)}</div>
                                </div>
                                <div style={{ background:'#D6F5E0', borderRadius:20, padding:'6px 14px', fontWeight:700, color:'#2A6B4A', height:'fit-content' }}>{p.stato}</div>
                            </div>
                        ))
                    }
                </div>
            </div>
        </div>
    )
}