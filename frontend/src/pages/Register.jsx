import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axios from 'axios'

export default function Register() {
    const [form, setForm] = useState({ email: '', password: '', nome: '', cognome: '', codiceFiscale: '', role: 'PAZIENTE' })
    const [errore, setErrore] = useState('')
    const navigate = useNavigate()

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

    const handleRegister = async (e) => {
        e.preventDefault()
        try {
            await axios.post('http://localhost:8080/api/auth/register', form)
            alert('Registrazione OK!')
            navigate('/login')
        } catch (err) {
            setErrore(err.response?.data || 'Errore registrazione')
        }
    }

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <form onSubmit={handleRegister} style={{ background: 'white', padding: '30px', borderRadius: '10px', width: '400px' }}>
                <h2>Sanitatrix - Registrazione</h2>
                <input name="nome" placeholder="Nome" onChange={handleChange} required style={{ width: '100%', marginBottom: '10px', padding: '10px' }} />
                <input name="cognome" placeholder="Cognome" onChange={handleChange} required style={{ width: '100%', marginBottom: '10px', padding: '10px' }} />
                <input name="codiceFiscale" placeholder="Codice Fiscale (16 caratteri)" maxLength={16} onChange={handleChange} required style={{ width: '100%', marginBottom: '10px', padding: '10px' }} />
                <input name="email" type="email" placeholder="Email" onChange={handleChange} required style={{ width: '100%', marginBottom: '10px', padding: '10px' }} />
                <input name="password" type="password" placeholder="Password" onChange={handleChange} required style={{ width: '100%', marginBottom: '10px', padding: '10px' }} />
                <select name="role" value={form.role} onChange={handleChange} style={{ width: '100%', padding: '10px', marginBottom: '10px' }}>
                    <option value="PAZIENTE">PAZIENTE</option>
                    <option value="MEDICO">MEDICO</option>
                </select>
                {errore && <p style={{ color: 'red' }}>{errore}</p>}
                <button type="submit" style={{ width: '100%', padding: '10px', background: '#2563eb', color: 'white' }}>Registrati</button>
                <p><Link to="/login">Hai già account? Login</Link></p>
            </form>
        </div>
    )
}