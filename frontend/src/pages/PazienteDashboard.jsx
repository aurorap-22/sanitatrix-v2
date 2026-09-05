import { useEffect, useState } from 'react'
export default function PazienteDashboard(){
    const [medici,setMedici]=useState([]);
    const [slots,setSlots]=useState([]);
    const [data,setData]=useState(new Date().toISOString().split('T')[0]);
    const [medicoId,setMedicoId]=useState("");
    const [referti,setReferti]=useState([]);

    useEffect(()=>{
        fetch("http://localhost:8080/api/medici").then(r=>r.json()).then(setMedici);
        const pid=localStorage.getItem("pazienteId");
        fetch(`http://localhost:8080/api/referti/paziente/${pid}`).then(r=>r.json()).then(setReferti);
    },[])

    const caricaSlot=()=>{
        fetch(`http://localhost:8080/api/disponibilita/slot/${medicoId}/${data}`).then(r=>r.json()).then(setSlots)
    }

    const prenota=async(ora)=>{
        const inizio=`${data}T${ora}`;
        const res=await fetch("http://localhost:8080/api/prenotazioni",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({medico:{id:medicoId},paziente:{id:localStorage.getItem("pazienteId")},dataOra:inizio})})
        if(res.ok) alert("Prenotato!"); else alert("Slot occupato")
    }

    return <div style={{padding:20}}><h2 style={{color:"#C8A2C8"}}>Prenota visita</h2>
        <select onChange={e=>setMedicoId(e.target.value)}><option>Seleziona medico</option>{medici.map(m=><option key={m.id} value={m.id}>{m.nome} {m.cognome}</option>)}</select>
        <input type="date" value={data} onChange={e=>setData(e.target.value)}/><button onClick={caricaSlot} style={{background:"#b5e8e8",marginLeft:10, padding:5, borderRadius:8, border:"none"}}>Vedi slot</button>
        <div style={{display:"flex",gap:10,flexWrap:"wrap",marginTop:20}}>{slots.map(s=><button key={s} onClick={()=>prenota(s)} style={{background:"#C8A2C8",color:"white",padding:10,borderRadius:10,border:"none"}}>{s}</button>)}</div>
        <h2 style={{color:"#C8A2C8", marginTop:40}}>I tuoi referti</h2>
        {referti.map(r=><div key={r.id} style={{background:"white",padding:15,marginBottom:10,borderRadius:15,border:"2px solid #b5e8e8"}}><b>{r.dataReferto}</b><br/>Diagnosi: {r.diagnosi}<br/>Terapia: {r.terapia}<br/>Esami: {r.esamiConsigliati}</div>)}
    </div>
}