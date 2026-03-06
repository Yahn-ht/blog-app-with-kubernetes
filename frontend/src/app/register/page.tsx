"use client";
import { useState } from 'react';
import { Container, Paper, TextField, Button, Typography, Box, Grid, Alert } from '@mui/material';
import { useAuthMutations } from '@/src/services/authService';
import { useRouter } from 'next/navigation';
import Navbar from '@/src/components/Navbar';
import Link from 'next/link';

export default function RegisterPage() {
    const router = useRouter();
    const { registerMutation } = useAuthMutations();
    const [form, setForm] = useState({
        email: '',
        firstName: '',
        lastName: '',
        password: ''
    });
    const [error, setError] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        
        registerMutation.mutate(form, {
            onSuccess: () => {
                router.push('/login?registered=true');
            },
            onError: (err: any) => {
                setError(err.response?.data?.detail || "Une erreur est survenue lors de l'inscription.");
            }
        });
    };

    return (
        <>
            <Navbar />
            <Container maxWidth="sm" sx={{ mt: 8 }}>
                <Paper sx={{ p: 4 }}>
                    <Typography variant="h5" textAlign="center" gutterBottom sx={{ fontWeight: 700 }}>
                        Créer un compte
                    </Typography>
                    
                    {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

                    <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                        <Grid container spacing={2}>
                            <Grid size={{ xs: 12, sm: 6 }}>
                                <TextField
                                    label="Prénom"
                                    fullWidth
                                    required
                                    value={form.firstName}
                                    onChange={(e) => setForm({ ...form, firstName: e.target.value })}
                                />
                            </Grid>
                            <Grid size={{ xs: 12, sm: 6 }}>
                                <TextField
                                    label="Nom"
                                    fullWidth
                                    required
                                    value={form.lastName}
                                    onChange={(e) => setForm({ ...form, lastName: e.target.value })}
                                />
                            </Grid>
                            <Grid size={{ xs: 12 }}>
                                <TextField
                                    label="Email"
                                    type="email"
                                    fullWidth
                                    required
                                    value={form.email}
                                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                                />
                            </Grid>
                            <Grid size={{ xs: 12 }}>
                                <TextField
                                    label="Mot de passe"
                                    type="password"
                                    fullWidth
                                    required
                                    value={form.password}
                                    onChange={(e) => setForm({ ...form, password: e.target.value })}
                                    helperText="Minimum 5 caractères"
                                />
                            </Grid>
                        </Grid>

                        <Button
                            type="submit"
                            variant="contained"
                            fullWidth
                            size="large"
                            sx={{ mt: 4 }}
                            disabled={registerMutation.isPending}
                        >
                            {registerMutation.isPending ? "Inscription..." : "S'inscrire"}
                        </Button>

                        <Box sx={{ mt: 3, textAlign: 'center' }}>
                            <Typography variant="body2">
                                Déjà un compte ?{" "}
                                <Link href="/login" style={{ color: '#1976d2', textDecoration: 'none', fontWeight: 600 }}>
                                    Connectez-vous
                                </Link>
                            </Typography>
                        </Box>
                    </Box>
                </Paper>
            </Container>
        </>
    );
}