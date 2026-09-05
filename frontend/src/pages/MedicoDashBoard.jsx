import { useEffect, useState } from 'react'
export default function MedicoDashboard(){
    const [prenotazioni,setPrenotazioni]=useState([]);
    const [selected, setSelected] = useState(null);
    const [form, setForm] = useState({ diagnosi:"", terapia:"", esamiConsigliati:"" });
    const medicoId = localStorage.getItem("medicoId");
    const carica = () => { fetch(`http://localhost:8080/api/prenotazioni/medico/${medicoId}`).then(r=>r.json()).then(setPrenotazioni) }
    useEffect(()=>{ carica() },[])
    const salva = async () => {
        const res = await fetch("http://localhost:8080/api/referti", {
            method:"POST", headers:{"Content-Type":"application/json"},
            body:JSON.stringify({ diagnosi: form.diagnosi, terapia: form.terapia, esamiConsigliati: form.esamiConsigliati, dataReferto: new Date().toISOString().split('T')[0], paziente: { id: selected.paziente.id }, prenotazione: { id: selected.id } })
        })
        if(res.ok){ alert("Referto salvato!"); setSelected(null); setForm({diagnosi:"", terapia:"", esamiConsigliati:""}); carica(); } else { alert("Errore: " + await res.text()); }
    }
    return (
        <div style={{padding:20, background:"#b5e8e8", minHeight:"100vh"}}>
            <h2 style={{color:"#C8A2C8"}}>Dashboard Medico</h2>
            {!selected? (<>{prenotazioni.map(p=><div key={p.id} style={{background:"white",padding:15,marginBottom:12,borderRadius:15,borderLeft:"6px solid #C8A2C8", display:"flex", justifyContent:"space-between"}}><div><b>{p.dataOra?.replace("T"," ").substring(0,16)}</b><br/>Paziente: {p.paziente?.nome} {p.paziente?.cognome}</div><button onClick={()=>setSelected(p)} style={{background:"#C8A2C8", color:"white", padding:"10px 18px", border:"none", borderRadius:12, height:45}}>Compila Referto</button></div>)}</>
            ) : (
                <div style={{background:"white", padding:22, borderRadius:20, maxWidth:600, margin:"auto", border:"3px solid #C8A2C8"}}>
                    <h3 style={{color:"#C8A2C8"}}>Nuovo Referto per {selected.paziente.nome}</h3>
                    <label>Diagnosi</label><textarea value={form.diagnosi} onChange={e=>setForm({...form, diagnosi:e.target.value})} style={{width:"100%", height:90, border:"2px solid #b5e8e8", borderRadius:10, padding:10, marginBottom:10}} />
                    <label>Terapia</label><textarea value={form.terapia} onChange={e=>setForm({...form, terapia:e.target.value})} style={{width:"100%", height:90, border:"2px solid #b5e8e8", borderRadius:10, padding:10, marginBottom:10}} />
                    <label>Esami Consigliati</label><textarea value={form.esamiConsigliati} onChange={e=>setForm({...form, esamiConsigliati:e.target.value})} style={{width:"100%", height:70, border:"2px solid #b5e8e8", borderRadius:10, padding:10, marginBottom:15}} />
                    <div style={{display:"flex", gap:10}}><button onClick={salva} style={{background:"#C8A2C8", color:"white", flex:1, padding:12, border:"none", borderRadius:10}}>SALVA</button><button onClick={()=>setSelected(null)} style={{background:"#ddd", flex:1, padding:12, border:"none", borderRadius:10}}>Annulla</button></div>
                </div>
            )}
        </div>
    )
}