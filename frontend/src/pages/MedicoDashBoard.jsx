import { useEffect, useState } from "react";
import axios from "axios";

export default function MedicoDashboard() {
    const [prenotazioni, setPrenotazioni] = useState([]);
    const [selected, setSelected] = useState(null);
    const [referto, setReferto] = useState({ diagnosi: "", prescrizione: "", note: "" });
    const [loading, setLoading] = useState(true);

    const user = JSON.parse(localStorage.getItem("user") || "{}");
    const token = localStorage.getItem("token");
    const config = { headers: { Authorization: `Bearer ${token}` } };

    // funzione che trova la data in qualunque campo tu abbia
    const getData = (p) => {
        return p.dataInizio || p.dataOra || p.data || p.dataAppuntamento || p.slot;
    };

    const formattaData = (p) => {
        const raw = getData(p);
        if (!raw) return "Data non disponibile";
        const d = new Date(raw);
        if (isNaN(d.getTime())) return String(raw); // se è già stringa tipo "09:00 28-09"
        return d.toLocaleString("it-IT");
    };

    const fetchPrenotazioni = async () => {
        try {
            const idMedico = user?.id || 1;
            const res = await axios.get(`http://localhost:8080/api/prenotazioni/medico/${idMedico}`, config);
            const lista = Array.isArray(res.data) ? res.data : res.data.content || [];
            console.log("Prenotazioni ricevute:", lista);
            setPrenotazioni(lista);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchPrenotazioni(); }, []);

    const salvaReferto = async () => {
        try {
            await axios.post(`http://localhost:8080/api/referti`, {
                prenotazione: { id: selected.id },
                diagnosi: referto.diagnosi,
                prescrizione: referto.prescrizione,
                note: referto.note
            }, config);
            alert("Referto salvato!");
            setSelected(null);
            setReferto({ diagnosi: "", prescrizione: "", note: "" });
            fetchPrenotazioni();
        } catch (e) {
            alert(e.response?.data || e.message);
        }
    };

    const inArrivo = prenotazioni.filter(p => (p.stato || "").toUpperCase() === "PRENOTATA");
    const storico = prenotazioni.filter(p => (p.stato || "").toUpperCase() !== "PRENOTATA");

    if (loading) return <div style={{padding:40}}>Caricamento...</div>;

    return (
        <div style={{ minHeight: "100vh", backgroundColor: "#F5F5F7", padding: "24px" }}>
            <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
                <h1 style={{ fontSize: "28px", fontWeight: "bold", color: "#7A6BC5" }}>Area Medico</h1>
                <p style={{ color: "#8FA8B0", marginBottom: "20px" }}>Ciao Dott. {user?.nome || "Fabio"}</p>

                <div style={{ display: "grid", gridTemplateColumns: "2fr 1fr", gap: "20px" }}>
                    <div>
                        <div style={{ backgroundColor: "white", borderLeft: "6px solid #C8B6FF", borderRadius: "16px", padding: "16px", marginBottom: "16px" }}>
                            <h2 style={{ fontWeight: "bold", color: "#7A6BC5", marginBottom: "12px" }}>Appuntamenti in arrivo ({inArrivo.length})</h2>
                            {inArrivo.map(p => (
                                <div key={p.id} style={{ backgroundColor: "#F0EFFF", padding: "12px", borderRadius: "12px", marginBottom: "8px", display: "flex", justifyContent: "space-between" }}>
                                    <div>
                                        <div style={{ fontWeight: "600", color: "#5A4E8C" }}>
                                            {p.paziente?.nome || p.nomePaziente || p.paziente?.username || "Paziente"} - {p.motivo || "Visita"}
                                        </div>
                                        <div style={{ fontSize: "12px", color: "#666" }}>{formattaData(p)}</div>
                                    </div>
                                    <button onClick={() => setSelected(p)} style={{ backgroundColor: "#B8E0E6", border: "none", padding: "8px 14px", borderRadius: "20px", fontWeight: "600", cursor: "pointer" }}>Referto</button>
                                </div>
                            ))}
                        </div>

                        <div style={{ backgroundColor: "white", borderRadius: "16px", padding: "16px" }}>
                            <h2 style={{ fontWeight: "bold", color: "#7A6BC5", marginBottom: "12px" }}>Storico ({storico.length}) - Appuntamenti conclusi o annullati</h2>
                            {storico.length===0 && <p style={{fontSize:"13px", color:"#aaa"}}>Quando completi un referto, l'appuntamento finirà qui</p>}
                            {storico.map(p => (
                                <div key={p.id} style={{ border: "1px solid #eee", padding: "10px", borderRadius: "10px", marginBottom: "6px", fontSize: "13px" }}>
                                    {formattaData(p)} - {p.paziente?.nome || "Paziente"} - {p.stato}
                                </div>
                            ))}
                        </div>
                    </div>

                    <div style={{ backgroundColor: "#B8E0E6", borderRadius: "16px", padding: "20px", height: "fit-content", position: "sticky", top: "20px" }}>
                        <h2 style={{ fontWeight: "bold", color: "#2E5A62", marginBottom: "12px" }}>Compila Referto</h2>
                        {!selected ? <p style={{ fontSize: "13px", color: "#5A7A82" }}>Clicca su "Referto" per compilarlo.</p> : (
                            <>
                                <div style={{ backgroundColor: "rgba(255,255,255,0.7)", padding: "10px", borderRadius: "10px", marginBottom: "12px" }}>
                                    <div style={{ fontWeight: "bold", fontSize: "14px" }}>{selected.paziente?.nome || "Paziente"}</div>
                                    <div style={{ fontSize: "12px" }}>{formattaData(selected)}</div>
                                </div>
                                <label style={{ fontSize: "12px", fontWeight: "600" }}>Diagnosi</label>
                                <textarea style={{ width: "100%", borderRadius: "10px", border: "none", padding: "10px", marginBottom: "10px" }} rows="4" value={referto.diagnosi} onChange={e => setReferto({...referto, diagnosi: e.target.value})} />
                                <label style={{ fontSize: "12px", fontWeight: "600" }}>Prescrizione</label>
                                <textarea style={{ width: "100%", borderRadius: "10px", border: "none", padding: "10px", marginBottom: "10px" }} rows="3" value={referto.prescrizione} onChange={e => setReferto({...referto, prescrizione: e.target.value})} />
                                <label style={{ fontSize: "12px", fontWeight: "600" }}>Note</label>
                                <textarea style={{ width: "100%", borderRadius: "10px", border: "none", padding: "10px", marginBottom: "14px" }} rows="2" value={referto.note} onChange={e => setReferto({...referto, note: e.target.value})} />
                                <button onClick={salvaReferto} style={{ width: "100%", backgroundColor: "#C8B6FF", color: "white", border: "none", padding: "12px", borderRadius: "20px", fontWeight: "bold", cursor: "pointer" }}>Salva Referto</button>
                                <button onClick={() => setSelected(null)} style={{ width: "100%", background: "none", border: "none", marginTop: "8px", cursor: "pointer" }}>Annulla</button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}