"use client";
import { useState, useEffect } from 'react';
import { Container, Paper, TextField, Button, Typography, Box, Grid, Alert } from '@mui/material';
import { useAuth } from '@/src/context/AuthContext';
import { useUserMutations } from '@/src/services/userService';
import Navbar from '@/src/components/Navbar';
import { useRouter } from 'next/navigation';

export default function ProfilePage() {
    const { user, isLoading } = useAuth();
    const router = useRouter();
    const { updateProfile, updatePassword } = useUserMutations();
    const [info, setInfo] = useState({ firstName: '', lastName: '' });
    const [pwd, setPwd] = useState({ oldPassword: '', newPassword: '' });

    useEffect(() => {
        if (!isLoading && !user) router.push('/login');
        if (user) setInfo({ firstName: user.firstName, lastName: user.lastName });
    }, [user, isLoading, router]);

    if (isLoading || !user) return null;

    return (
        <>
            <Navbar />
            <Container maxWidth="md">
                <Grid container spacing={4}>
                    <Grid size={{ xs: 12, md: 6 }}>
                        <Paper sx={{ p: 3 }}>
                            <Typography variant="h6" gutterBottom>Profile Details</Typography>
                            <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2 }} 
                                onSubmit={(e) => { e.preventDefault(); updateProfile.mutate(info); }}>
                                <TextField label="First Name" fullWidth value={info.firstName} onChange={e => setInfo({...info, firstName: e.target.value})} />
                                <TextField label="Last Name" fullWidth value={info.lastName} onChange={e => setInfo({...info, lastName: e.target.value})} />
                                <Button type="submit" variant="contained">Update Info</Button>
                            </Box>
                        </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, md: 6 }}>
                        <Paper sx={{ p: 3 }}>
                            <Typography variant="h6" gutterBottom>Change Password</Typography>
                            <Box component="form" sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}
                                onSubmit={(e) => { e.preventDefault(); updatePassword.mutate(pwd); }}>
                                <TextField label="Current Password" type="password" fullWidth onChange={e => setPwd({...pwd, oldPassword: e.target.value})} />
                                <TextField label="New Password" type="password" fullWidth onChange={e => setPwd({...pwd, newPassword: e.target.value})} />
                                <Button type="submit" variant="outlined" color="warning">Update Password</Button>
                            </Box>
                        </Paper>
                    </Grid>
                </Grid>
            </Container>
        </>
    );
}