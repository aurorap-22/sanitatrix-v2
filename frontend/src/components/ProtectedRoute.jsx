import { Navigate } from 'react-router-dom'

export default function ProtectedRoute({ children, allowedRoles }){
    const token = localStorage.getItem('token')
    const ruolo = localStorage.getItem('ruolo')
    if(!token) return <Navigate to="/login" replace />
    if(allowedRoles && !allowedRoles.includes(ruolo)) return <Navigate to="/login" replace />
    return children
}