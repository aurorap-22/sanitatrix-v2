import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import PazienteDashboard from "./pages/PazienteDashboard.jsx";
import MedicoDashboard from "./pages/MedicoDashboard.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";

export default function App(){
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/paziente" element={
                    <ProtectedRoute allowedRoles={["PAZIENTE"]}>
                        <PazienteDashboard />
                    </ProtectedRoute>
                } />
                <Route path="/medico" element={
                    <ProtectedRoute allowedRoles={["MEDICO"]}>
                        <MedicoDashboard />
                    </ProtectedRoute>
                } />
            </Routes>
        </BrowserRouter>
    );
}