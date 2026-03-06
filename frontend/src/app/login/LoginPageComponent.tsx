// "use client"; // <- Obligatoire
// import { useState } from 'react';
// import { Container, Paper, TextField, Button, Typography, Box, Alert } from '@mui/material';
// import { useAuthMutations } from '@/src/services/authService';
// import { useAuth } from '@/src/context/AuthContext';
// import { useRouter, useSearchParams } from 'next/navigation';
// import Navbar from '@/src/components/Navbar';

// export default function LoginPageComponent() {
//   const searchParams = useSearchParams();
//   const isRegistered = searchParams.get('registered') === 'true';
//   const [form, setForm] = useState({ email: '', password: '' });
//   const { loginMutation } = useAuthMutations();
//   const { login } = useAuth();
//   const router = useRouter();

//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
//     loginMutation.mutate(form, {
//       onSuccess: async (data) => {
//         await login(data.token);
//         router.push('/');
//       }
//     });
//   };

//   return (
//     <>
//       <Navbar />
//       <Container maxWidth="xs" sx={{ mt: 10 }}>
//         <Paper sx={{ p: 4 }}>
//           {isRegistered && (
//             <Alert severity="success" sx={{ mb: 2 }}>
//               Inscription réussie ! Connectez-vous.
//             </Alert>
//           )}
//           <Typography variant="h5" textAlign="center" gutterBottom>Login</Typography>
//           <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
//             <TextField label="Email" type="email" fullWidth required onChange={e => setForm({...form, email: e.target.value})} />
//             <TextField label="Password" type="password" fullWidth required onChange={e => setForm({...form, password: e.target.value})} />
//             <Button type="submit" variant="contained" size="large" disabled={loginMutation.isPending}>
//               {loginMutation.isPending ? 'Logging in...' : 'Login'}
//             </Button>
//           </Box>
//         </Paper>
//       </Container>
//     </>
//   );
// }