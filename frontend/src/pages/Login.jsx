import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
const API = "http://localhost:8080/api";
export default function Login(){
    const [email,setEmail]=useState("");
    const [password,setPassword]=useState("");
    const [err,setErr]=useState("");
    const nav=useNavigate();
    const go=async(e)=>{
        e.preventDefault();
        setErr("");
        try{
            const medici=await fetch(`${API}/medici`).then(r=>r.json());
            const m=medici.find(x=>x.utente?.email.toLowerCase()===email.toLowerCase());
            if(m){ localStorage.setItem("medicoId",m.id); localStorage.setItem("nome",m.nome); nav("/medico"); return; }
            const pazienti=await fetch(`${API}/pazienti`).then(r=>r.json());
            const p=pazienti.find(x=>x.utente?.email.toLowerCase()===email.toLowerCase());
            if(p){ localStorage.setItem("pazienteId",p.id); localStorage.setItem("nome",p.nome); nav("/paziente"); return; }
            setErr("Email non trovata");
        }catch{ setErr("Backend non raggiungibile su 8080"); }
    };
    return(
        <div style={{minHeight:"100vh",background:"#F3F4F6",display:"flex",justifyContent:"center",alignItems:"center"}}>
            <form onSubmit={go} style={{background:"white",borderRadius:28,padding:32,width:390,boxSizing:"border-box"}}>
                <div style={{background:"#E9E0FF",borderRadius:20,padding:18,textAlign:"center",marginBottom:20}}><b style={{color:"#3D2A6B",fontSize:22}}>Sanitatrix</b></div>
                {err&&<div style={{background:"#FFE1E1",color:"#9A1C1C",borderRadius:12,padding:10,fontSize:12,marginBottom:12}}>{err}</div>}
                <input value={email} onChange={e=>setEmail(e.target.value)} type="email" placeholder="Email" required style={{width:"100%",padding:12,borderRadius:12,border:"1px solid #EDEAF6",marginBottom:10}}/>
                <input value={password} onChange={e=>setPassword(e.target.value)} type="password" placeholder="Password" required style={{width:"100%",padding:12,borderRadius:12,border:"1px solid #EDEAF6",marginBottom:16}}/>
                <button style={{width:"100%",background:"#3D2A6B",color:"white",border:0,borderRadius:14,padding:13,fontWeight:800}}>Entra →</button>
                <div style={{textAlign:"center",marginTop:14,fontSize:13}}><Link to="/register" style={{color:"#3D2A6B",fontWeight:800,textDecoration:"none"}}>Registrati</Link></div>
            </form>
        </div>
    );
}