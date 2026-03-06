"use client";
import { useParams, useRouter } from 'next/navigation';
import { useState } from 'react';
import { Container, Typography, Box, Paper, Divider, TextField, Button, Avatar, List, ListItem, ListItemAvatar, ListItemText, IconButton, Alert } from '@mui/material';
import { useArticleBySlug, useArticleMutations } from '@/src/services/articleService';
import { useCommentsByArticle, useCommentMutations } from '@/src/services/commentService';
import { useAuth } from '@/src/context/AuthContext';
import Navbar from '@/src/components/Navbar';
import FavoriteIcon from '@mui/icons-material/Favorite';
import DeleteIcon from '@mui/icons-material/Delete';

export default function ArticleDetailPage() {
    const { slug } = useParams();
    const router = useRouter();
    const { user } = useAuth();
    const [commentText, setCommentText] = useState("");

    const { data: article, isLoading: articleLoading } = useArticleBySlug(slug as string);
    const { data: comments, isLoading: commentsLoading } = useCommentsByArticle(article?.id as number);
    const { likeArticle, unlikeArticle } = useArticleMutations();
    const { createComment, deleteComment } = useCommentMutations(article?.id as number);

    const handleSendComment = (e: React.FormEvent) => {
        e.preventDefault();
        if (!user) return router.push(`/login?redirect=/articles/${slug}`);
        if (!commentText.trim()) return;

        createComment.mutate(commentText, {
            onSuccess: () => setCommentText("")
        });
    };

    if (articleLoading) return <Typography sx={{ p: 4 }}>Chargement de l'article...</Typography>;
    if (!article) return <Typography sx={{ p: 4 }}>Article introuvable.</Typography>;

    return (
        <>
            <Navbar />
            <Container maxWidth="md" sx={{ pb: 8 }}>
                {/* Header de l'article */}
                <Box sx={{ mb: 4 }}>
                    <Typography variant="overline" color="primary" sx={{ fontWeight: 'bold' }}>
                        {article.category.name}
                    </Typography>
                    <Typography variant="h3" component="h1" gutterBottom sx={{ fontWeight: 800 }}>
                        {article.title}
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, color: 'text.secondary' }}>
                        <Typography variant="body2">
                            Par <strong>{article.author.firstName} {article.author.lastName}</strong>
                        </Typography>
                        <Typography variant="body2">•</Typography>
                        <Typography variant="body2">
                            Publié le {new Date(article.publishedAt).toLocaleDateString()}
                        </Typography>
                    </Box>
                </Box>

                {/* Bouton Like */}
                <Box sx={{ mb: 4, display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Button
                        variant="outlined"
                        color="error"
                        startIcon={<FavoriteIcon />}
                        onClick={() => user ? likeArticle.mutate(article.id) : router.push('/login')}
                    >
                        Like ({article.likeCount})
                    </Button>
                </Box>

                <Divider sx={{ mb: 4 }} />

                {/* Contenu de l'article */}
                <Typography variant="body1" sx={{ fontSize: '1.2rem', lineHeight: 1.8, whiteSpace: 'pre-wrap' }}>
                    {article.content}
                </Typography>

                <Divider sx={{ my: 6 }} />

                {/* Section Commentaires */}
                <Box id="comments">
                    <Typography variant="h5" gutterBottom sx={{ fontWeight: 700 }}>
                        Commentaires ({comments?.totalElements || 0})
                    </Typography>

                    {/* Formulaire de commentaire */}
                    <Box component="form" onSubmit={handleSendComment} sx={{ mt: 3, mb: 5 }}>
                        <TextField
                            fullWidth
                            multiline
                            rows={3}
                            placeholder={user ? "Ajouter un commentaire..." : "Connectez-vous pour laisser un commentaire"}
                            value={commentText}
                            onChange={(e) => setCommentText(e.target.value)}
                            disabled={!user && !articleLoading}
                            sx={{ mb: 2 }}
                        />
                        {user ? (
                            <Button
                                type="submit"
                                variant="contained"
                                disabled={createComment.isPending || !commentText.trim()}
                            >
                                {createComment.isPending ? "Envoi..." : "Publier"}
                            </Button>
                        ) : (
                            <Button variant="outlined" onClick={() => router.push(`/login?redirect=/articles/${slug}`)}>
                                Se connecter pour commenter
                            </Button>
                        )}
                    </Box>

                    {/* Liste des commentaires */}
                    {commentsLoading ? (
                        <Typography>Chargement des commentaires...</Typography>
                    ) : (
                        <List>
                            {comments?.content?.map((comment) => (
                                <Paper key={comment.id} variant="outlined" sx={{ mb: 2, p: 1 }}>
                                    <ListItem alignItems="flex-start">
                                        <ListItemAvatar>
                                            <Avatar sx={{ bgcolor: 'primary.main' }}>
                                                {comment.user.firstName[0]}
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText
                                            primary={`${comment.user.firstName} ${comment.user.lastName}`}
                                            secondary={comment.content}
                                            primaryTypographyProps={{ fontWeight: 600 }}
                                        />
                                        {user?.id === comment.user.id && (
                                            <IconButton
                                                edge="end"
                                                color="error"
                                                onClick={() => deleteComment.mutate(comment.id)}
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        )}
                                    </ListItem>
                                </Paper>
                            ))}

                            {comments?.empty && (
                                <Typography color="text.secondary">
                                    Soyez le premier à commenter cet article.
                                </Typography>
                            )}
                        </List>
                    )}
                </Box>
            </Container>
        </>
    );
}