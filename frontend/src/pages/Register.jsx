import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api";

const TIPO_VISITA = ["VISITA_BASE","VISITA_CONTROLLO","CARDIOLOGIA","DERMATOLOGIA","ORTOPEDIA","GINECOLOGIA","OCULISTICA"];
const inputStyle = {padding:"12px 14px", borderRadius:12, border:"1.5px solid #ddd6fe", background:"#faf5ff", color:"#4c1d95", width:"100%", boxSizing:"border-box"};

export default function Register() {
    const navigate = useNavigate();
    const [err, setErr] = useState("");
    const [loading, setLoading] = useState(false);
    const [form, setForm] = useState({
        nome:"", cognome:"", email:"", password:"",
        codiceFiscale:"", ruolo:"PAZIENTE",
        dataNascita:"", telefono:"", indirizzo:"",
        specializzazione:"CARDIOLOGIA"
    });
    const onChange = e => setForm({...form, [e.target.name]: e.target.value});
    const onSubmit = async e => {
        e.preventDefault();
        setErr("");
        setLoading(true);
        const payload = {...form, codiceFiscale: form.codiceFiscale.toUpperCase().trim()};
        if(payload.ruolo==="PAZIENTE") delete payload.specializzazione;
        else { delete payload.dataNascita; delete payload.indirizzo; }
        try{
            await api.post("/auth/register", payload);
            alert("Registrato! Ora puoi effettuare il login.");
            navigate("/login");
        }catch(e){
            const msg = e.response?.data?.message || e.response?.data || e.message || "Errore durante la registrazione";
            setErr(typeof msg === "string" ? msg : "Errore durante la registrazione");
        } finally {
            setLoading(false);
        }
    };
    return (
        <div style={{minHeight:"100vh", background:"#f5f3ff", display:"flex", justifyContent:"center", alignItems:"center", padding:20}}>
            <div style={{background:"white", padding:32, borderRadius:24, width:480, boxShadow:"0 20px 40px rgba(139,92,246,0.15)", border:"1px solid #ede9fe"}}>
                <h2 style={{color:"#7c3aed", textAlign:"center", fontWeight:800}}>Sanitatrix 💜</h2>
                <p style={{textAlign:"center", color:"#a78bfa", marginBottom:24}}>Crea account</p>
                {err && <div style={{background:"#FFE1E1",color:"#9A1C1C",borderRadius:12,padding:10,fontSize:12,marginBottom:12}}>{err}</div>}
                <form onSubmit={onSubmit} style={{display:"flex", flexDirection:"column", gap:14}}>
                    <select name="ruolo" value={form.ruolo} onChange={onChange} style={inputStyle}>
                        <option value="PAZIENTE">PAZIENTE</option><option value="MEDICO">MEDICO</option>
                    </select>
                    <div style={{display:"flex", gap:12}}><input name="nome" placeholder="Nome" value={form.nome} onChange={onChange} required style={inputStyle}/><input name="cognome" placeholder="Cognome" value={form.cognome} onChange={onChange} required style={inputStyle}/></div>
                    <input name="email" placeholder="Email" value={form.email} onChange={onChange} required style={inputStyle}/>
                    <input name="password" type="password" placeholder="Password" value={form.password} onChange={onChange} required style={inputStyle}/>
                    <input name="codiceFiscale" placeholder="Codice Fiscale" value={form.codiceFiscale} onChange={onChange} required maxLength={16} style={{...inputStyle, textTransform:"uppercase"}}/>
                    <input name="telefono" placeholder="Telefono" value={form.telefono} onChange={onChange} required style={inputStyle}/>
                    {form.ruolo==="PAZIENTE" ? (<><input name="dataNascita" type="date" value={form.dataNascita} onChange={onChange} required style={inputStyle}/><input name="indirizzo" placeholder="Indirizzo" value={form.indirizzo} onChange={onChange} required style={inputStyle}/></>) : (<select name="specializzazione" value={form.specializzazione} onChange={onChange} style={inputStyle}>{TIPO_VISITA.map(t=><option key={t} value={t}>{t}</option>)}</select>)}
                    <button type="submit" disabled={loading} style={{background:"linear-gradient(135deg,#8b5cf6,#7c3aed)", color:"white", padding:14, borderRadius:12, border:"none", fontWeight:700, cursor:loading?"not-allowed":"pointer", opacity:loading?0.6:1}}>
                        {loading ? "Registrazione in corso..." : "Registrati"}
                    </button>
                </form>
                <div style={{textAlign:"center",marginTop:14,fontSize:13}}>
                    <Link to="/login" style={{color:"#7c3aed",fontWeight:800,textDecoration:"none"}}>Torna al Login</Link>
                </div>
            </div>
        </div>
    )
}