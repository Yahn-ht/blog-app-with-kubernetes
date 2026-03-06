"use client";
import { AppBar, Toolbar, Typography, Button, TextField, Box, Container } from '@mui/material';
import Link from 'next/link';
import { useAuth } from '@/src/context/AuthContext';

export default function Navbar({ onSearch }: { onSearch?: (v: string) => void }) {
    const { user, logout } = useAuth();

    return (
        <AppBar position="sticky" color="inherit" elevation={1} sx={{ mb: 4 }}>
            <Container>
                <Toolbar disableGutters>
                    <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 700 }}>
                        <Link href="/" style={{ textDecoration: 'none', color: '#1976d2' }}>BLOGAPP</Link>
                    </Typography>

                    {onSearch && (
                        <TextField
                            size="small"
                            placeholder="Search articles..."
                            onChange={(e) => onSearch(e.target.value)}
                            sx={{ mr: 2, width: 250 }}
                        />
                    )}

                    <Box sx={{ display: 'flex', gap: 1 }}>
                        {user ? (
                            <>
                                <Button component={Link} href="/articles/create">Create</Button>
                                <Button component={Link} href="/articles/manage">Mes Articles</Button>
                                <Button component={Link} href="/profile">Profile</Button>
                                <Button onClick={logout} color="error">Logout</Button>
                            </>
                        ) : (
                            <>
                                <Button component={Link} href="/login">Login</Button>
                                <Button component={Link} href="/register" variant="contained">Register</Button>
                            </>
                        )}
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}