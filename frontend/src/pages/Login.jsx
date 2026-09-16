import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api";

export default function Login(){
    const [email,setEmail]=useState("");
    const [password,setPassword]=useState("");
    const [err,setErr]=useState("");
    const [loading,setLoading]=useState(false);
    const nav=useNavigate();

    const go=async(e)=>{
        e.preventDefault();
        setErr("");
        setLoading(true);
        try{
            localStorage.clear();

            const res = await api.post("/auth/login", { email, password });
            const data = res.data;

            localStorage.setItem("token", data.token);
            localStorage.setItem("ruolo", data.ruolo);
            localStorage.setItem("utenteId", data.utenteId);
            localStorage.setItem("email", data.email);
            localStorage.setItem("nome", data.nome || "");
            localStorage.setItem("cognome", data.cognome || "");

            if(data.ruolo === "MEDICO"){
                localStorage.setItem("medicoId", data.medicoId);
                if(data.specializzazione) localStorage.setItem("specializzazione", data.specializzazione);
                nav("/medico");
            } else if(data.ruolo === "PAZIENTE"){
                localStorage.setItem("pazienteId", data.pazienteId);
                nav("/paziente");
            } else {
                setErr("Ruolo non riconosciuto");
            }
        }catch(e){
            if(e.response?.data?.error){
                setErr(e.response.data.error);
            } else if(e.response?.status === 401){
                setErr("Credenziali errate");
            } else {
                setErr("Backend non raggiungibile su 8080 - avvia il backend e il database");
            }
        } finally {
            setLoading(false);
        }
    };

    return(
        <div style={{minHeight:"100vh",background:"#F3F4F6",display:"flex",justifyContent:"center",alignItems:"center"}}>
            <form onSubmit={go} style={{background:"white",borderRadius:28,padding:32,width:390,boxSizing:"border-box"}}>
                <div style={{background:"#E9E0FF",borderRadius:20,padding:18,textAlign:"center",marginBottom:20}}><b style={{color:"#3D2A6B",fontSize:22}}>Sanitatrix</b></div>
                {err&&<div style={{background:"#FFE1E1",color:"#9A1C1C",borderRadius:12,padding:10,fontSize:12,marginBottom:12}}>{err}</div>}
                <input value={email} onChange={e=>setEmail(e.target.value)} type="email" placeholder="Email" required style={{width:"100%",padding:12,borderRadius:12,border:"1px solid #EDEAF6",marginBottom:10}}/>
                <input value={password} onChange={e=>setPassword(e.target.value)} type="password" placeholder="Password" required style={{width:"100%",padding:12,borderRadius:12,border:"1px solid #EDEAF6",marginBottom:16}}/>
                <button disabled={loading} style={{width:"100%",background:"#3D2A6B",color:"white",border:0,borderRadius:14,padding:13,fontWeight:800,cursor:loading?"not-allowed":"pointer",opacity:loading?0.6:1}}>
                    {loading ? "Accesso in corso..." : "Entra →"}
                </button>
                <div style={{textAlign:"center",marginTop:14,fontSize:13}}><Link to="/register" style={{color:"#3D2A6B",fontWeight:800,textDecoration:"none"}}>Registrati</Link></div>
            </form>
        </div>
    );
}