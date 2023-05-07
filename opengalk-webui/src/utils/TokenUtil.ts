import jwtDecode from 'jwt-decode'

const key = 'token'

export const getToken = (): any => {
    return localStorage.getItem(key)
}

export const setToken = (token: any) => {
    return localStorage.setItem(key, token)
}

export const removeToken = () => {
    return localStorage.removeItem(key)
}

export const getAccountByToken = () => {
    return jwtDecode<any>(getToken()).account
}

export const getAuthorityByToken = () => {
    return jwtDecode<any>(getToken()).authority
}

export const getIdByToken = () => {
    return jwtDecode<any>(getToken()).id
}



