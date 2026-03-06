"use client";
import { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { Container, Paper, TextField, Button, Typography, Box, MenuItem, Alert } from '@mui/material';
import api from '@/src/lib/axios';
import Navbar from '@/src/components/Navbar';
import { useArticleById, useArticleMutations } from '@/src/services/articleService';
import { ArticleCategory } from '@/src/types';

export default function EditArticlePage() {
    const { id } = useParams();
    const router = useRouter();
    const { data: article, isLoading } = useArticleById(Number(id));
    const { updateArticle } = useArticleMutations();
    const [categories, setCategories] = useState<ArticleCategory[]>([]);
    const [form, setForm] = useState({
        title: '',
        description: '',
        content: '',
        categoryId: ''
    });

    useEffect(() => {
        api.get('/categories').then(res => setCategories(res.data));
    }, []);

    useEffect(() => {
        if (article) {
            setForm({
                title: article.title,
                description: article.description,
                content: article.content,
                categoryId: article.category.id.toString()
            });
        }
    }, [article]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        updateArticle.mutate({ 
            id: Number(id), 
            payload: { ...form, categoryId: Number(form.categoryId) } 
        }, {
            onSuccess: () => router.push('/articles/manage')
        });
    };

    if (isLoading) return <Typography sx={{ p: 4 }}>Chargement...</Typography>;

    return (
        <>
            <Navbar />
            <Container maxWidth="md">
                <Paper sx={{ p: 4 }}>
                    <Typography variant="h5" sx={{ mb: 4, fontWeight: 700 }}>Modifier l'article</Typography>
                    <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        <TextField 
                            label="Titre" 
                            fullWidth 
                            required 
                            value={form.title}
                            onChange={e => setForm({...form, title: e.target.value})} 
                        />
                        <TextField 
                            select 
                            label="Catégorie" 
                            fullWidth 
                            required 
                            value={form.categoryId} 
                            onChange={e => setForm({...form, categoryId: e.target.value})}
                        >
                            {categories.map(c => (
                                <MenuItem key={c.id} value={c.id.toString()}>{c.name}</MenuItem>
                            ))}
                        </TextField>
                        <TextField 
                            label="Description" 
                            multiline 
                            rows={2} 
                            fullWidth 
                            required 
                            value={form.description}
                            onChange={e => setForm({...form, description: e.target.value})} 
                        />
                        <TextField 
                            label="Contenu" 
                            multiline 
                            rows={12} 
                            fullWidth 
                            required 
                            value={form.content}
                            onChange={e => setForm({...form, content: e.target.value})} 
                        />
                        <Box sx={{ display: 'flex', gap: 2 }}>
                            <Button type="submit" variant="contained" size="large" disabled={updateArticle.isPending}>
                                Enregistrer les modifications
                            </Button>
                            <Button variant="outlined" onClick={() => router.back()}>
                                Annuler
                            </Button>
                        </Box>
                    </Box>
                </Paper>
            </Container>
        </>
    );
}