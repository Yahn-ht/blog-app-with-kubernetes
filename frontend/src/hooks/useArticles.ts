import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/src/lib/axios';
import { Article, PageResponse } from '@/src/types';

export const useArticles = (page: number, size: number, searchKey?: string) => {
    return useQuery<PageResponse<Article>>({
        queryKey: ['articles', page, size, searchKey],
        queryFn: async () => {
            const response = await api.get('/articles', {
                params: { page, size, searchKey }
            });
            return response.data;
        }
    });
};

export const useArticleActions = () => {
    const queryClient = useQueryClient();

    const likeMutation = useMutation({
        mutationFn: (id: number) => api.post(`/articles/${id}/like`),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['articles'] })
    });

    return { likeMutation };
};