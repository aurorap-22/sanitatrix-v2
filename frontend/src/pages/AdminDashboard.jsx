import { useEffect, useState } from 'react'
export default function AdminDashboard(){
    const [pagamenti,setPagamenti]=useState([]);
    useEffect(()=>{ fetch("http://localhost:8080/api/pagamenti").then(r=>r.json()).then(setPagamenti) },[])
    const cambiaStato=async(id,stato)=>{ await fetch(`http://localhost:8080/api/pagamenti/${id}/stato?stato=${stato}`,{method:"PUT"}); window.location.reload() }
    return <div style={{padding:20}}><h2 style={{color:"#C8A2C8"}}>Resoconto Admin</h2>
        <table style={{width:"100%"}}><thead><tr><th>ID</th><th>Importo</th><th>Stato</th><th>Azione</th></tr></thead><tbody>
        {pagamenti.map(p=><tr key={p.id}><td>{p.id}</td><td>{p.importo}</td><td>{p.stato}</td><td><button onClick={()=>cambiaStato(p.id,"PAGATO")} style={{background:"#b5e8e8", border:"none", padding:5, borderRadius:5}}>Segna PAGATO</button></td></tr>)}
        </tbody></table>
    </div>
}