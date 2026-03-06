"use client";
import { useState } from 'react';
import { Container, Paper, TextField, Button, Typography, Box } from '@mui/material';
import { useArticleMutations } from '@/src/services/articleService';
import { useRouter } from 'next/navigation';
import Navbar from '@/src/components/Navbar';

export default function CreateArticle() {
    const [form, setForm] = useState({ title: '', description: '', content: '', categoryId: 1, publishedAt: new Date().toISOString() });
    const { createArticle } = useArticleMutations();
    const router = useRouter();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        createArticle.mutate(form, { onSuccess: () => router.push('/') });
    };

    return (
        <>
            <Navbar />
            <Container maxWidth="md">
                <Paper sx={{ p: 4 }}>
                    <Typography variant="h5" gutterBottom>Create Article</Typography>
                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        <TextField label="Title" fullWidth onChange={e => setForm({...form, title: e.target.value})} />
                        <TextField label="Description" multiline rows={2} fullWidth onChange={e => setForm({...form, description: e.target.value})} />
                        <TextField label="Content" multiline rows={10} fullWidth onChange={e => setForm({...form, content: e.target.value})} />
                        <Button type="submit" variant="contained" size="large">Publish</Button>
                    </Box>
                </Paper>
            </Container>
        </>
    );
}