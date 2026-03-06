import { useMutation } from '@tanstack/react-query';
import api from '@/src/lib/axios';
import { LoginResponse } from '@/src/types';

export const useAuthMutations = () => {
    const loginMutation = useMutation({
        mutationFn: async (payload: any) => {
            const { data } = await api.post<LoginResponse>('/auth/login', payload);
            return data;
        }
    });

    const registerMutation = useMutation({
        mutationFn: (payload: any) => api.post('/auth/register', payload)
    });

    return { loginMutation, registerMutation };
};