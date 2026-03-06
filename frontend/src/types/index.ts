export interface User {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: 'ADMIN' | 'USER';
}

export interface ArticleCategory {
    id: number;
    name: string;
}

export interface Article {
    id: number;
    title: string;
    content: string;
    description: string;
    publishedAt: string;
    likeCount: number;
    slug: string;
    author: User;
    category: ArticleCategory;
}

export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    last: boolean;
    first: boolean;
    empty: boolean;
}

export interface LoginResponse {
    token: string;
}

export interface CommentDto {
    id: number;
    content: string;
    user: User;
}