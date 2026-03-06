"use client";
import { useState } from 'react';
import { Container, Grid, Card, CardContent, Typography, Pagination, Box, Button } from '@mui/material';
import { useArticles, useArticleMutations } from '@/src/services/articleService';
import Navbar from '@/src/components/Navbar';
import Link from 'next/link';
import { useAuth } from '@/src/context/AuthContext';

export default function HomePage() {
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const { data, isLoading } = useArticles(page, 6, search);
    const { user } = useAuth();
    const { likeArticle } = useArticleMutations();

    return (
        <>
            <Navbar onSearch={(v) => { setSearch(v); setPage(0); }} />
            <Container>
                {isLoading ? (
                    <Typography>Loading...</Typography>
                ) : (
                    <>
                        <Grid container spacing={3}>
                            {data?.content.map((art) => (
                                <Grid size={{ xs: 12, sm: 6, md: 4 }} key={art.id}>
                                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                                        <CardContent sx={{ flexGrow: 1 }}>
                                            <Typography variant="caption" color="primary">{art.category.name}</Typography>
                                            <Typography variant="h6" sx={{ mt: 1 }}>{art.title}</Typography>
                                            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{art.description}</Typography>
                                        </CardContent>
                                        <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <Button component={Link} href={`/articles/${art.slug}`} size="small">Read More</Button>
                                            <Button size="small" onClick={() => user ? likeArticle.mutate(art.id) : window.location.href='/login'}>
                                                ❤️ {art.likeCount}
                                            </Button>
                                        </Box>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                        <Box sx={{ py: 6, display: 'flex', justifyContent: 'center' }}>
                            <Pagination 
                                count={data?.totalPages || 0} 
                                page={page + 1} 
                                onChange={(_, v) => setPage(v - 1)} 
                                color="primary" 
                            />
                        </Box>
                    </>
                )}
            </Container>
        </>
    );
}