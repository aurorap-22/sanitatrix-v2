import { useState, useEffect} from "react";
import { BrowserRouter, Routes, Route, Link, useNavigate} from "react-router-dom";
import "./App.css";
function Login() {
    const nav = useNavigate();
    const [email, setEmail]= useState(""); const [pass, setPass]= useState(" ");
    const login = (e)=>{ e.preventDefault(); localStorage.setItem("user", email); nav("/prenota"); }
  return(
      <div className="page"><div className="card">
          <div className="header-dot"></div>
          <h1>BENTORNATA</h1><p className="sub">Accedi a Sanitatrix</p>
          <form onSubmit={login}>
              <label>Email</label><input value={email} onChange={e=>setEmail(e.target.value)} required placeholder="aurora@email.com"/>
              <label>Password</label><input type="password" value={pass} onChange={e=>setPass(e.target.value)} required placeholder="********"/>
              <button>Accedi</button>
          </form>
          <p className="link-text">Non hai ancora il tuo account? <Link to="/register">Registrati</Link> </p>
      </div> </div>
  )
}


function Register(){
    const nav = useNavigate();
    const [email, setEmail]= useState("");
    const [pass, setPass]= useState("");
    const [cf, setCf]= useState(" ");
    const [ruolo, setRuolo] = useState("PAZIENTE");

    const reg=async (e)=>{
        e.preventeDefault();
        if(cf.length!==16){ alert("Il codice fiscale deve essere di 16 caratteri"); return;}
        try{
            const res = await fetch("http://localhost:8080")
        }

    }



}
export default App