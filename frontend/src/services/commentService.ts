import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/src/lib/axios';
import { CommentDto , PageResponse} from '@/src/types';

export const useCommentsByArticle = (articleId: number, page: number = 0) => {
    return useQuery<PageResponse<CommentDto>>({
        queryKey: ['comments', articleId, page],
        queryFn: async () => {
            const { data } = await api.get(`/comments/article/${articleId}`, {
                params: { page, size: 10 }
            });
            return data;
        },
        enabled: !!articleId
    });
};

export const useCommentMutations = (articleId: number) => {
    const queryClient = useQueryClient();

    const createComment = useMutation({
        mutationFn: (content: string) => api.post('/comments', { content, articleId }),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['comments', articleId] })
    });

    const deleteComment = useMutation({
        mutationFn: (commentId: number) => api.delete(`/comments/${commentId}`),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['comments', articleId] })
    });

    return { createComment, deleteComment };
};