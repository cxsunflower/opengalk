// 将一个个题目字符串拆解/组合成题目对象

export const subjectToArray = (allSubjects: string): any[] => {
    let subjectArray: string[] = allSubjects.split(/\n\n/)
    let subjectObjectArray: any[] = []

    for (const key in subjectArray) {
        let subjectObject = {
            type: 0,                // 类型，0为单选，1为多选
            subject: '',            // 题干
            answer: <string[]>[],   // 正确答案
            items: <any>[],         // 题目选项
            error: ''               // 错误提示
        }

        let subject = subjectArray[key]
        let answerArray = subject.match(/\n\s*答案[:：]/)

        if (answerArray == null) {
            subjectObject.error = '题目缺少答案'
        }

        // 匹配到多个答案
        if (answerArray != null && answerArray.length > 1) {
            subjectObject.error = '每道题只能有一个答案'
        }

        // 拆分题干与选项
        let selectionReg = /\n\s*答案[:：]\s*[A-Z]+/i;
        let sourceTimu = subject.replace(selectionReg, '')

        let sourceTimuArr = sourceTimu.split(/[A-Z][.、． ]/ig)

        if (sourceTimuArr.length == 1) {
            subjectObject.error = '选项不能为空'
        }

        let valArr: string[] = [];
        sourceTimuArr.forEach((item: string, i: number) => {
            sourceTimuArr[i] = item.trim().replace(/\s+/g, ' ')

            if (i === 0) {
                // 题干
                subjectObject.subject = sourceTimuArr[i]
            } else {
                // 选项
                let selectionObject = {
                    picture: '',
                    text: sourceTimuArr[i],
                    value: String.fromCharCode(65 + i - 1) // ascii转字母
                }

                subjectObject.items.push(selectionObject)
                valArr.push(selectionObject.value)
            }
        })

        /**
         * 单选题和多选题
         */
        if (subject.search(/\n\s*[A-Z][.、． ]/ig) > -1) {
            // 单选题和多选题
            let selectAns = subject.match(selectionReg)

            if (selectAns === null) {
                subjectObject.error = '答案不对应'
            }

            if (selectAns) {
                let answer = selectAns[0].trim().replace(/^答案[:：]\s*/g, '').toUpperCase()

                // 判断单选题多选题
                if (answer.length > 1) {
                    subjectObject.type = 1
                }

                for (let i = 0; i < answer.length; i++) {
                    subjectObject.answer.push(answer[i])
                }

            }
        }

        // 单选题错误
        if (subjectObject.type === 0 && !valArr.includes(subjectObject.answer[0])) {
            subjectObject.error = '单选题答案选项不正确或无答案'
        }

        // 多选题错误
        if (subjectObject.type === 1) {
            for (let i of subjectObject.answer) {
                if (!valArr.includes(i)) {
                    subjectObject.error = '多选题答案选项不正确或无答案'
                    break
                }
            }
        }

        subjectObjectArray.push(subjectObject)
    }

    return subjectObjectArray
}

export const arrayToSubject = (subjectArray: any[]): string => {
    let allSubjects = ''
    for (let i = 0; i < subjectArray.length; i++) {
        allSubjects += subjectArray[i].subject + '\n'
            + 'A、' + subjectArray[i].optionA + '\n'
            + 'B、' + subjectArray[i].optionB + '\n'
            + 'C、' + subjectArray[i].optionC + '\n'
            + 'D、' + subjectArray[i].optionD + '\n'

        if (i != subjectArray.length - 1) {
            allSubjects += '答案：' + subjectArray[i].answer + '\n\n'
        } else {
            allSubjects += '答案：' + subjectArray[i].answer
        }
    }
    return allSubjects
}
