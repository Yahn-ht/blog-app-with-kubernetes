import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/src/lib/axios';
import { User } from '@/src/types';

export const useProfile = (enabled: boolean) => {
    return useQuery<User>({
        queryKey: ['profile'],
        queryFn: async () => {
            const { data } = await api.get('/users/profile');
            return data;
        },
        enabled
    });
};

export const useUserMutations = () => {
    const queryClient = useQueryClient();

    const updateProfile = useMutation({
        mutationFn: (payload: any) => api.put('/users/profile', payload),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['profile'] })
    });

    const updatePassword = useMutation({
        mutationFn: (payload: any) => api.put('/users/password', payload)
    });

    return { updateProfile, updatePassword };
};