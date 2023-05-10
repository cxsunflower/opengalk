export const formatTime = (data: any): string => {
    if (data == null) {
        return '无';
    }
    const date = new Date(data);
    const hours: string = date.getHours() < 10 ? '0' + date.getHours() : date.getHours().toString();
    const minutes: string = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes().toString();
    const seconds: string = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds().toString();
    return (
        date.getFullYear() +
        "-" +
        (date.getMonth() + 1) +
        "-" +
        date.getDate() +
        " " +
        hours +
        ":" +
        minutes +
        ":" +
        seconds
    );
};

export const formatAuthority = (authority: string | number) => {
    if (typeof authority == 'number') {
        switch (authority) {
            case 0:
                return '管理员'
            case 1:
                return '教师'
            case 2:
                return '学生'
            default:
                return ''
        }
    } else {
        switch (authority) {
            case '管理员':
                return 0
            case '教师':
                return 1
            case '学生':
                return 2
            default:
                return 2
        }
    }
};

export const formatGender = (gender: number | string): any => {
    if (typeof gender === 'string') {
        switch (gender) {
            case "男":
                return 0
            case "女":
                return 1
            default:
                return 0
        }
    } else {
        switch (gender) {
            case 0:
                return '男'
            case 1:
                return '女'
            default:
                return ''
        }
    }
}

export const formatBirthday = (birthday: string, isAdd: number): string => {
    if (birthday != null) {
        const date = new Date(birthday)
        if (isAdd === 1) {
            date.setDate(date.getDate() + 1);
        }
        return date.getFullYear() +
            '-' +
            (date.getMonth() + 1) +
            '-' +
            date.getDate()
    } else {
        return ''
    }
}

export const formatIsScored = (row: any) => {
    switch (row.isScored) {
        case 0:
            return '未评分';
        case 1:
            return '已评分';
        default:
            return ''
    }
};

export const formatIsLocked = (row: any) => {
    switch (row.isLocked) {
        case 0:
            return '未锁定'
        case 1:
            return '已锁定'
        default:
            return ''
    }

}

export const formatPaperType = (type: number) => {
    switch (type) {
        case 0:
            return '公安专业科目'
        case 1:
            return '行政职业能力测试'
        default:
            return ''
    }
}

export const formatSubjectType = (type: number) => {
    switch (type) {
        case 0:
            return '单'
        case 1:
            return '多'
        default:
            return ''
    }
}

