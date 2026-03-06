import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/src/lib/axios';
import { Article, PageResponse } from '@/src/types';

export const useArticles = (page: number, size: number, searchKey?: string, categoryId?: number) => {
    return useQuery<PageResponse<Article>>({
        queryKey: ['articles', page, size, searchKey, categoryId],
        queryFn: async () => {
            const { data } = await api.get('/articles', {
                params: { page, size, searchKey, categoryId }
            });
            return data;
        }
    });
};

export const useArticleById = (id: number) => {
    return useQuery<Article>({
        queryKey: ['article', id],
        queryFn: async () => {
            const { data } = await api.get(`/articles/${id}`);
            return data;
        },
        enabled: !!id
    });
};

export const useArticleBySlug = (slug: string) => {
    return useQuery<Article>({
        queryKey: ['article', slug],
        queryFn: async () => {
            const { data } = await api.get(`/articles/slug/${slug}`);
            return data;
        },
        enabled: !!slug
    });
};

export const useMyArticles = (page: number, size: number) => {
    return useQuery<PageResponse<Article>>({
        queryKey: ['my-articles', page],
        queryFn: async () => {
            const { data } = await api.get('/articles/user/mine', {
                params: { page, size }
            });
            return data;
        }
    });
};

export const useArticleMutations = () => {
    const queryClient = useQueryClient();

    const createArticle = useMutation({
        mutationFn: (payload: any) => api.post('/articles', payload),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['articles', 'my-articles'] })
    });

    const updateArticle = useMutation({
        mutationFn: ({ id, payload }: { id: number, payload: any }) => api.put(`/articles/${id}`, payload),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['articles', 'my-articles', 'article'] })
    });

    const deleteArticle = useMutation({
        mutationFn: (id: number) => api.delete(`/articles/${id}`),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['articles', 'my-articles'] })
    });

    const likeArticle = useMutation({
        mutationFn: (id: number) => api.post(`/articles/${id}/like`),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['articles'] })
    });

    const unlikeArticle = useMutation({
        mutationFn: (id: number) => api.post(`/articles/${id}/unlike`),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['articles'] });
            queryClient.invalidateQueries({ queryKey: ['article'] });
            queryClient.invalidateQueries({ queryKey: ['my-articles'] });
        }
    });

    return { createArticle, updateArticle, deleteArticle, likeArticle, unlikeArticle };
};