import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import PazienteDashboard from './pages/PazienteDashboard.jsx'
import MedicoDashBoard from './pages/MedicoDashBoard.jsx'
import AdminDashboard from './pages/AdminDashboard.jsx'

export default function App(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/paziente" element={<PazienteDashboard />} />
                <Route path="/medico" element={<MedicoDashBoard />} />
                <Route path="/admin" element={<AdminDashboard />} />
            </Routes>
        </BrowserRouter>
    )
}