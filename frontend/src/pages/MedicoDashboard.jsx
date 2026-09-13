import { useEffect,useState } from "react";
export default function MedicoDashboard(){
    const [list,setList]=useState([]);
    const [medico,setMedico]=useState(null);
    const MID=Number(localStorage.getItem("medicoId")||1);
    const carica=()=>{ fetch(`http://localhost:8080/api/medici/${MID}`).then(r=>r.json()).then(setMedico); fetch(`http://localhost:8080/api/prenotazioni/medico/${MID}`).then(r=>r.json()).then(d=>setList(Array.isArray(d)?d:[])); };
    useEffect(()=>{carica()},[]);

    const formatOrario = (p) => {
        if (p.orario) return p.orario.slice(0,5); // 14:30:00 -> 14:30
        if (p.dataOra) return new Date(p.dataOra).toLocaleTimeString("it-IT",{hour:"2-digit",minute:"2-digit"});
        if (p.data) return new Date(p.data).toLocaleTimeString("it-IT",{hour:"2-digit",minute:"2-digit"});
        return "--:--";
    };

    return(
        <div style={{background:"#F3F4F6",minHeight:"100vh",padding:24}}>
            <div style={{maxWidth:900,margin:"0 auto"}}>
                <div style={{background:"#E9E0FF",borderRadius:28,padding:22,textAlign:"center",marginBottom:16}}><b style={{fontSize:26,color:"#3D2A6B"}}>Area Medico</b><div style={{color:"#7A6AA8"}}>Dott. {medico?.nome} {medico?.cognome} {medico?.specializzazione&&`- ${medico.specializzazione}`}</div></div>
                <div style={{background:"#D6E8FF",borderRadius:28,padding:20}}><b style={{color:"#1E3A5F"}}>Appuntamenti ({list.length})</b>{list.length===0?<div style={{marginTop:10,color:"#5A8AC7"}}>Nessuno</div>:list.map(p=><div key={p.id} style={{background:"white",borderRadius:14,padding:10,marginTop:8,display:"flex",justifyContent:"space-between",alignItems:"center"}}><span><b style={{color:"#3D2A6B"}}>{formatOrario(p)}</b> - {p.prestazione?.nome || p.tipoVisita || "Visita Ortopedica"} - {p.paziente?.nome} {p.paziente?.cognome}</span><span style={{background:"#D6F5E0",borderRadius:12,padding:"4px 10px",fontSize:11}}>OK</span></div>)}</div>
                <div style={{textAlign:"center",marginTop:14}}><button onClick={carica} style={{border:0,background:"white",borderRadius:20,padding:"8px 16px"}}>🔄 Aggiorna</button></div>
            </div>
        </div>
    );
}