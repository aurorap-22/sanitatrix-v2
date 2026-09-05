import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axios from 'axios'

export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [errore, setErrore] = useState('')
    const navigate = useNavigate()

    const handleLogin = async (e) => {
        e.preventDefault()
        setErrore('')
        try {
            const res = await axios.post('http://localhost:8080/api/auth/login', {
                username,
                password
            })

            // salva token e ruolo
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('role', res.data.role)
            localStorage.setItem('username', res.data.username)

            const role = res.data.role

            if (role === 'PAZIENTE') {
                navigate('/paziente')
            } else if (role === 'MEDICO') {
                navigate('/medico')
            } else if (role === 'ADMIN') {
                navigate('/admin')
            }

        } catch (err) {
            console.error(err)
            setErrore('Username o password errati')
        }
    }

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', background: '#f5f5f5' }}>
            <form onSubmit={handleLogin} style={{ background: 'white', padding: '30px', borderRadius: '10px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)', width: '350px' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#7c3aed' }}>Sanitatrix - Login</h2>

                <div style={{ marginBottom: '15px' }}>
                    <label>Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        style={{ width: '100%', padding: '10px', marginTop: '5px', borderRadius: '5px', border: '1px solid #ccc' }}
                    />
                </div>

                <div style={{ marginBottom: '15px' }}>
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={{ width: '100%', padding: '10px', marginTop: '5px', borderRadius: '5px', border: '1px solid #ccc' }}
                    />
                </div>

                {errore && <p style={{ color: 'red', textAlign: 'center' }}>{errore}</p>}

                <button type="submit" style={{ width: '100%', padding: '10px', background: '#7c3aed', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }}>
                    Accedi
                </button>

                <p style={{ textAlign: 'center', marginTop: '15px' }}>
                    Non hai un account? <Link to="/register">Registrati</Link>
                </p>
            </form>
        </div>
    )
}