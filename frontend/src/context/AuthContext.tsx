"use client";
import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User } from '@/src/types';
import api from '@/src/lib/axios';

interface AuthContextType {
    user: User | null;
    token: string | null;
    isLoading: boolean;
    login: (token: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    const loadProfile = async (t: string) => {
        try {
            const { data } = await api.get('/users/profile', {
                headers: { Authorization: `Bearer ${t}` }
            });
            setUser(data);
            setToken(t);
        } catch (e) {
            logout();
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        const t = localStorage.getItem('token');
        if (t) loadProfile(t);
        else setIsLoading(false);
    }, []);

    const login = async (t: string) => {
        setIsLoading(true);
        localStorage.setItem('token', t);
        await loadProfile(t);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
        setToken(null);
        setIsLoading(false);
    };

    return (
        <AuthContext.Provider value={{ user, token, isLoading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("AuthProvider missing");
    return context;
};