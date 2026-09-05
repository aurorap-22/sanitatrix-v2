import { useState, useEffect } from 'react'
import axios from 'axios'

export default function PazienteDashboard() {
    const [prenotazioni, setPrenotazioni] = useState([])
    const [medici, setMedici] = useState([])
    const [prestazioni, setPrestazioni] = useState([])
    const [referti, setReferti] = useState([])
    const [tab, setTab] = useState('prenotazioni')
    const [loading, setLoading] = useState(true)
    const [form, setForm] = useState({ idMedico: '', idPrestazione: '', dataOra: '' })
    const [pazienteId, setPazienteId] = useState(null)

    const utenteId = localStorage.getItem('id')

    const caricaTutto = async () => {
        setLoading(true)
        let pid = null

        // Trova paziente vero dall'utente loggato
        try {
            const res = await axios.get(`http://localhost:8080/api/pazienti/utente/${utenteId}`)
            pid = res.data.id
        } catch {
            try {
                const all = await axios.get(`http://localhost:8080/api/pazienti`)
                const mio = all.data.find(p => p.utente?.id == utenteId)
                if (mio) pid = mio.id
            } catch {}
        }
        if (!pid) pid = utenteId
        setPazienteId(pid)

        try {
            const r1 = await axios.get(`http://localhost:8080/api/prenotazioni/prenotazioni/paziente/${pid}`)
            setPrenotazioni(r1.data)
        } catch {}

        try {
            const r2 = await axios.get(`http://localhost:8080/api/referti/paziente/${pid}`)
            setReferti(r2.data)
        } catch {}

        try { const r3 = await axios.get(`http://localhost:8080/api/medici`); setMedici(r3.data) } catch {}
        try { const r4 = await axios.get(`http://localhost:8080/api/prestazione`); setPrestazioni(r4.data) } catch {}

        setLoading(false)
    }

    useEffect(() => { caricaTutto() }, [])

    const handlePrenota = async (e) => {
        e.preventDefault()
        const dataOra = new Date(form.dataOra)
        const dataFine = new Date(dataOra.getTime() + 60*60*1000)
        const payload = {
            dataOra: dataOra.toISOString(),
            dataFine: dataFine.toISOString(),
            stato: "PRENOTATA",
            paziente: { id: Number(pazienteId) },
            medico: { id: Number(form.idMedico) },
            prestazione: { id: Number(form.idPrestazione) }
        }
        try {
            await axios.post(`http://localhost:8080/api/prenotazioni/add-prenotazione`, payload)
            alert("Prenotazione creata!")
            setForm({ idMedico: '', idPrestazione: '', dataOra: '' })
            caricaTutto()
        } catch { alert("Errore prenotazione") }
    }

    if (loading) return <div style={{ padding: '40px' }}>Caricamento...</div>

    return (
        <div style={{ padding: '20px', background: '#f0f7ff', minHeight: '100vh', fontFamily: 'sans-serif' }}>
            <div style={{ background: 'white', padding: '20px', borderRadius: '12px', display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
                <h1 style={{ color: '#2563eb', margin: 0 }}>Dashboard Paziente</h1>
                <button onClick={() => { localStorage.clear(); window.location.href = '/' }} style={{ background: '#ef4444', color: 'white', border: 'none', padding: '8px 14px', borderRadius: '6px' }}>Logout</button>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 360px', gap: '20px' }}>
                <div style={{ background: 'white', padding: '20px', borderRadius: '12px' }}>
                    <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
                        <button onClick={() => setTab('prenotazioni')} style={{ padding: '8px 16px', borderRadius: '20px', border: 'none', background: tab==='prenotazioni'?'#2563eb':'#e5e7eb', color: tab==='prenotazioni'?'white':'black', fontWeight: 'bold' }}>Prenotazioni ({prenotazioni.length})</button>
                        <button onClick={() => setTab('referti')} style={{ padding: '8px 16px', borderRadius: '20px', border: 'none', background: tab==='referti'?'#16a34a':'#e5e7eb', color: tab==='referti'?'white':'black', fontWeight: 'bold' }}>Storico Referti ({referti.length})</button>
                    </div>

                    {tab === 'prenotazioni' ? (
                        prenotazioni.length === 0 ? <p>Nessuna prenotazione.</p> :
                            prenotazioni.map(p => (
                                <div key={p.id} style={{ border: '1px solid #dbeafe', padding: '14px', borderRadius: '10px', marginBottom: '12px', background: '#f8fafc' }}>
                                    <b>📅 {new Date(p.dataOra).toLocaleString()}</b><br/>
                                    👨‍⚕️ {p.medico?.nome} {p.medico?.cognome} - {p.prestazione?.nome} ({p.prestazione?.prezzo}€)<br/>
                                    <span style={{ background: '#fef3c7', padding: '2px 8px', borderRadius: '10px' }}>{p.stato}</span>
                                </div>
                            ))
                    ) : (
                        referti.length === 0 ? <p>Nessun referto ancora.</p> :
                            referti.map(r => (
                                <div key={r.id} style={{ border: '1px solid #bbf7d0', background: '#f0fdf4', padding: '15px', borderRadius: '10px', marginBottom: '12px' }}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                                        <b style={{ color: '#15803d' }}>Referto #{r.id}</b>
                                        <span style={{ fontSize: '13px' }}>{r.dataReferto ? new Date(r.dataReferto).toLocaleDateString() : ''}</span>
                                    </div>
                                    <p><b>Diagnosi:</b> {r.diagnosi}</p>
                                    <p><b>Terapia:</b> {r.terapia}</p>
                                    <p><b>Esami consigliati:</b> {r.esamiConsigliati}</p>
                                    {r.prenotazione && <small>Visita del {new Date(r.prenotazione.dataOra).toLocaleDateString()}</small>}
                                </div>
                            ))
                    )}
                </div>

                <div style={{ background: 'white', padding: '20px', borderRadius: '12px', height: 'fit-content' }}>
                    <h3 style={{ textAlign: 'center' }}>Nuova Prenotazione</h3>
                    <form onSubmit={handlePrenota} style={{ display: 'flex', flexDirection: 'column', gap: '12px', marginTop: '15px' }}>
                        <select required value={form.idMedico} onChange={e => setForm({...form, idMedico: e.target.value})} style={{ padding: '10px' }}>
                            <option value="">Medico</option>
                            {medici.map(m => <option key={m.id} value={m.id}>{m.nome} {m.cognome} - {m.specializzazione}</option>)}
                        </select>
                        <select required value={form.idPrestazione} onChange={e => setForm({...form, idPrestazione: e.target.value})} style={{ padding: '10px' }}>
                            <option value="">Prestazione</option>
                            {prestazioni.map(pr => <option key={pr.id} value={pr.id}>{pr.nome} - {pr.prezzo}€</option>)}
                        </select>
                        <input required type="datetime-local" value={form.dataOra} onChange={e => setForm({...form, dataOra: e.target.value})} style={{ padding: '10px' }} />
                        <button type="submit" style={{ padding: '12px', background: '#2563eb', color: 'white', border: 'none', borderRadius: '8px', fontWeight: 'bold' }}>PRENOTA</button>
                    </form>
                </div>
            </div>
        </div>
    )
}