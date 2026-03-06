"use client";
import { useState, useEffect } from 'react';
import { Container, Typography, Paper, Button, Box, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, Pagination, Alert } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useMyArticles, useArticleMutations } from '@/src/services/articleService';
import { useAuth } from '@/src/context/AuthContext';
import Navbar from '@/src/components/Navbar';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

export default function ManageArticlesPage() {
    const { user, isLoading: authLoading } = useAuth();
    const router = useRouter();
    const [page, setPage] = useState(0);
    const { data, isLoading } = useMyArticles(page, 8);
    const { deleteArticle } = useArticleMutations();

    useEffect(() => {
        if (!authLoading && !user) router.push('/login');
    }, [user, authLoading, router]);

    const handleDelete = (id: number) => {
        if (window.confirm("Voulez-vous vraiment supprimer cet article ?")) {
            deleteArticle.mutate(id);
        }
    };

    if (authLoading || !user) return null;

    return (
        <>
            <Navbar />
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
                    <Typography variant="h4" sx={{ fontWeight: 800 }}>Mes Articles</Typography>
                    <Button variant="contained" component={Link} href="/articles/create">
                        Nouvel Article
                    </Button>
                </Box>

                {deleteArticle.isSuccess && <Alert severity="success" sx={{ mb: 2 }}>Article supprimé.</Alert>}

                <TableContainer component={Paper} variant="outlined">
                    <Table>
                        <TableHead sx={{ bgcolor: '#f5f5f5' }}>
                            <TableRow>
                                <TableCell sx={{ fontWeight: 'bold' }}>Titre</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Catégorie</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Date</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }}>Likes</TableCell>
                                <TableCell sx={{ fontWeight: 'bold' }} align="right">Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {isLoading ? (
                                <TableRow><TableCell colSpan={5} align="center">Chargement...</TableCell></TableRow>
                            ) : data?.content.map((art) => (
                                <TableRow key={art.id} hover>
                                    <TableCell>{art.title}</TableCell>
                                    <TableCell>{art.category.name}</TableCell>
                                    <TableCell>{new Date(art.publishedAt).toLocaleDateString()}</TableCell>
                                    <TableCell>{art.likeCount}</TableCell>
                                    <TableCell align="right">
                                        <IconButton color="primary" component={Link} href={`/articles/edit/${art.id}`}>
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton color="error" onClick={() => handleDelete(art.id)}>
                                            <DeleteIcon />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {!isLoading && data?.empty && (
                                <TableRow>
                                    <TableCell colSpan={5} align="center">Vous n'avez pas encore écrit d'articles.</TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>

                <Box sx={{ mt: 4, display: 'flex', justifyContent: 'center' }}>
                    <Pagination 
                        count={data?.totalPages || 0} 
                        page={page + 1} 
                        onChange={(_, v) => setPage(v - 1)} 
                        color="primary" 
                    />
                </Box>
            </Container>
        </>
    );
}