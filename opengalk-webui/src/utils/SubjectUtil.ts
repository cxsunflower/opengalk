import {Selection, Subject, SubjectDTO, SubjectObject} from "../data";

/**
 * 将一个个题目字符串拆解/组合成题目对象
 * 一坨大便
 * @param allSubjects
 */
export const subjectToArray = (allSubjects: string): SubjectObject[] => {
    let subjectArray: string[] = allSubjects.split(/\n\n/);
    let subjectObjectArray: any[] = [];

    for (const key in subjectArray) {

        let subjectObject: SubjectObject = {
            type: 0,                // 类型，0为单选，1为多选
            subject: <Subject>{},   // 题干
            answer: <string[]>[],   // 正确答案
            items: <Selection[]>[], // 题目选项
            error: ''               // 错误提示
        };

        let subject = subjectArray[key];
        // 匹配答案二字
        let answerText = subject.match(/\n\s*答案[:：]/);

        if (answerText == null) {
            subjectObject.error = '题目缺少答案';
        }

        // 匹配到多个答案
        if (answerText != null && answerText.length > 1) {
            subjectObject.error = '每道题只能有一个答案';
        }

        // 拆分答案
        let selectionReg = /\n\s*答案[:：]\s*[A-Z]+/i;
        let selectAns = subject.match(selectionReg) as string[];

        /**
         * 单选题和多选题
         */
        if (selectAns != null) {
            let answer = selectAns[0].trim().replace(/^答案[:：]\s*/g, '').toUpperCase();

            // 判断单选题多选题
            if (answer.length > 1) {
                subjectObject.type = 1;
            }

            for (let i = 0; i < answer.length; i++) {
                subjectObject.answer.push(answer[i]);
            }
        }

        // 拆分题干与选项
        let sourceTimu = subject.replace(selectionReg, '');
        let sourceTimuArr = sourceTimu.split(/[A-Z][.、． ]/ig);

        if (sourceTimuArr.length == 1) {
            subjectObject.error = '选项不能为空';
        }

        let valArr: string[] = [];
        sourceTimuArr.forEach((item: string, i: number) => {
            sourceTimuArr[i] = item.trim().replace(/\s+/g, '');
            // console.log("source:" + i + ":" + sourceTimuArr[i]);

            if (i === 0) {
                let imgs = sourceTimuArr[i].split(/\s*data:image/) as string[];
                // console.log('imgs0:' + imgs[0]);
                // console.log('imgs1:' + imgs[1]);
                if (imgs.length == 0) {
                    subjectObject.error = '缺少题干';
                } else {
                    for (let j = 1; j < imgs.length; j++) {
                        imgs[j] = imgs[j].replaceAll('\n', '');
                    }
                }
                // 题干
                subjectObject.subject = {
                    content: imgs[0],
                    imgs: imgs.slice(1),
                };

            } else {
                // 选项
                let selectionObject: Selection = {
                    imgs: <string[]>[],
                    text: sourceTimuArr[i],
                    value: String.fromCharCode(65 + i - 1) // ascii转字母
                };

                subjectObject.items.push(selectionObject);
                valArr.push(selectionObject.value);
            }
        });

        // 单选题错误
        if (subjectObject.type === 0 && !valArr.includes(subjectObject.answer[0])) {
            subjectObject.error = '单选题答案选项不正确或无答案';
        }

        // 多选题错误
        if (subjectObject.type === 1) {
            for (let i of subjectObject.answer) {
                if (!valArr.includes(i)) {
                    subjectObject.error = '多选题答案选项不正确或无答案';
                    break;
                }
            }
        }

        subjectObjectArray.push(subjectObject);
    }

    return subjectObjectArray;
};

/**
 * 将题目对象转为字符串数组
 * 一坨大便
 * @param subjectArray
 */
export const arrayToSubject = (subjectArray: SubjectDTO[]): string => {
    let allSubjects = '';
    for (let i = 0; i < subjectArray.length; i++) {
        const tmp = subjectArray[i];
        allSubjects += '<p>' + tmp.subject + '</p>';
        if (tmp.hasImgs == 1) {

        }
        allSubjects += '<p>A、' + tmp.optionA + '</p>'
            + '<p>B、' + tmp.optionB + '</p>'
            + '<p>C、' + tmp.optionC + '</p>'
            + '<p>D、' + tmp.optionD + '</p>';

        if (i != subjectArray.length - 1) {
            allSubjects += '<p>答案：' + tmp.answer + '</p><p>&nbsp;</p>';
        } else {
            allSubjects += '<p>答案：' + tmp.answer + '</p>';
        }
    }
    return allSubjects;
};
