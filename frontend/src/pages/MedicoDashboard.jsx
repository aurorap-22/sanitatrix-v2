import { useEffect,useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export default function MedicoDashboard(){
    const nav = useNavigate();
    const [list,setList]=useState([]);
    const [medico,setMedico]=useState(null);
    const MID = localStorage.getItem("medicoId");

    const logout = () => {
        localStorage.clear();
        nav("/login");
    };

    const carica = () => {
        if(!MID) return;
        api.get(`/medici/${MID}`).then(r=>setMedico(r.data)).catch(err=>console.error(err));
        api.get(`/prenotazioni/medico/${MID}`).then(r=>setList(Array.isArray(r.data)?r.data:[])).catch(err=>console.error(err));
    };
    useEffect(()=>{carica()},[MID]);

    const formatDataOra = (p) => {
        if (!p.dataOra) return "--:--";
        const d = new Date(p.dataOra);
        return d.toLocaleDateString("it-IT",{day:"2-digit",month:"2-digit",year:"numeric"}) + " " +
            d.toLocaleTimeString("it-IT",{hour:"2-digit",minute:"2-digit"});
    };

    if(!MID) return <div style={{padding:30}}>Nessun medico loggato, <a onClick={()=>nav("/login")} style={{color:"#3D2A6B",cursor:"pointer",fontWeight:"bold"}}>torna al login</a></div>;

    return(
        <div style={{background:"#F3F4F6",minHeight:"100vh",padding:24}}>
            <div style={{maxWidth:900,margin:"0 auto"}}>
                <div style={{display:"flex",justifyContent:"space-between",alignItems:"center",marginBottom:16}}>
                    <div style={{background:"#E9E0FF",borderRadius:28,padding:22,textAlign:"center",flex:1,marginRight:12}}>
                        <b style={{fontSize:26,color:"#3D2A6B"}}>Area Medico</b>
                        <div style={{color:"#7A6AA8"}}>
                            Dott. {medico?.nome || localStorage.getItem("nome") || ""} {medico?.cognome || localStorage.getItem("cognome") || ""}
                            {(medico?.specializzazione || localStorage.getItem("specializzazione")) && ` - ${medico?.specializzazione || localStorage.getItem("specializzazione")}`}
                        </div>
                    </div>
                    <button onClick={logout} style={{background:"#fff",border:"1px solid #EDEAF6",color:"#3D2A6B",padding:"12px 20px",borderRadius:14,fontWeight:700,cursor:"pointer"}}>
                        Esci
                    </button>
                </div>
                <div style={{background:"#D6E8FF",borderRadius:28,padding:20}}>
                    <b style={{color:"#1E3A5F"}}>Appuntamenti ({list.length})</b>
                    {list.length===0 ? (
                        <div style={{marginTop:10,color:"#5A8AC7"}}>Nessun appuntamento</div>
                    ) : (
                        list.map(p=>(
                            <div key={p.id} style={{background:"white",borderRadius:14,padding:14,marginTop:8,display:"flex",justifyContent:"space-between",alignItems:"center"}}>
                                <div>
                                    <div><b style={{color:"#3D2A6B"}}>{formatDataOra(p)}</b></div>
                                    <div style={{fontSize:13,color:"#1E3A5F"}}>
                                        {p.prestazione?.nome || p.tipoVisita || "Visita"}
                                    </div>
                                    <div style={{fontSize:12,color:"#5A8AC7"}}>
                                        Paziente: {p.paziente?.nome} {p.paziente?.cognome}
                                        {p.paziente?.codiceFiscale ? ` (${p.paziente.codiceFiscale})` : ""}
                                    </div>
                                </div>
                                <span style={{background:p.stato==="PRENOTATA"?"#D6E8FF":"#D6F5E0",borderRadius:12,padding:"6px 12px",fontSize:12,fontWeight:700,color:p.stato==="PRENOTATA"?"#1E3A5F":"#2A6B4A"}}>
                                    {p.stato || "OK"}
                                </span>
                            </div>
                        ))
                    )}
                </div>
                <div style={{textAlign:"center",marginTop:14}}>
                    <button onClick={carica} style={{border:0,background:"white",borderRadius:20,padding:"10px 20px",cursor:"pointer",fontWeight:600,color:"#3D2A6B"}}>
                        🔄 Aggiorna
                    </button>
                </div>
            </div>
        </div>
    );
}