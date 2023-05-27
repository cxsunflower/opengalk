import * as cheerio from "cheerio";

export const htmlToText = (html: string): string => {
    const $ = cheerio.load(html);
    let result = '';
    console.log($.html());

    $('p' as any).each((index, element) => {
        const paragraph = $(element);
        const imgSrcList: string[] = [];
        paragraph.find('img').each((imgIndex, imgElement) => {
            const src = $(imgElement).attr('src');
            if (src) {
                imgSrcList.push(src);
            }
        });
        result += $(element).text() + '\n';
        for (const element of imgSrcList) {
            result += element;
        }

        if (paragraph.html() == "&nbsp;") {
            result += '\n\n';
        }

    });
    return result;
};