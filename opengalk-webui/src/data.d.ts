export interface ResponseResult {
    message: string,
    data: Result,
}

export interface Result {
    响应状态: number,
    响应消息: string,
    响应数据: any,
}

export interface Paper {
    id: string,
    name: string,
    score: number,
}

export interface Subject {
    content: string,
    imgs: string[],
}

export interface Selection {
    imgs: string[],
    text: string,
    value: string,
}

export interface SubjectObject {
    type: number,
    subject: Subject,
    answer: string[],
    items: Selection[],
    error: string,
}
