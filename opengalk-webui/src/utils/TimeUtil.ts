import {EventEmitter} from 'eventemitter3'
//设置格式
export interface RemainTimeData {
    /** 天数 */
    days: number
    /** 小时数 */
    hours: number
    /** 分钟数 */
    minutes: number
    /** 秒数 */
    seconds: number
    /** 毫秒数 */
    count: number
}

enum CountdownStatus {
    running,    // 运行中
    paused,     // 暂停
    stoped,     // 结束
}

export enum CountdownEventName {
    START = 'start',
    STOP = 'stop',
    RUNNING = 'running',
}

interface CountdownEventMap {
    [CountdownEventName.START]: []
    [CountdownEventName.STOP]: []
    [CountdownEventName.RUNNING]: [RemainTimeData, number]
}

export class Countdown extends EventEmitter<CountdownEventMap> {
    //定义参数
    private static COUNT_IN_MILLISECOND: number = 100;
    private static SECOND_IN_MILLISECOND: number = 10 * Countdown.COUNT_IN_MILLISECOND;
    private static MINUTE_IN_MILLISECOND: number = 60 * Countdown.SECOND_IN_MILLISECOND;
    private static HOUR_IN_MILLISECOND: number = 60 * Countdown.MINUTE_IN_MILLISECOND;
    private static DAY_IN_MILLISECOND: number = 24 * Countdown.HOUR_IN_MILLISECOND;
    //声明状态
    private readonly endTime: number
    private remainTime: number = 0
    private status: CountdownStatus = CountdownStatus.stoped
    private readonly step: number

    // 传入结束时间，多少秒跳动一次
    constructor(endTime: number, step: number = 1e3) {
        super()
        //赋值
        this.endTime = endTime
        this.step = step
        this.start()
    }


    public start() {
        // 通过emit通知外界，订阅
        this.emit(CountdownEventName.START)
        // 更新状态
        this.status = CountdownStatus.running
        this.countdown()
    }

    public stop() {
        this.emit(CountdownEventName.STOP)
        this.status = CountdownStatus.stoped
    }

    private countdown = () => {
        // 借鉴promise思想 判断状态不是running再执行具体逻辑
        if (this.status !== CountdownStatus.running) {
            return;
        }
        // 处理剩余时间，得到正数，如果是负数则会返回0
        this.remainTime = Math.max(this.endTime - Date.now(), 0)
        // 报告状态  传入格式参数与原始数据
        this.emit(CountdownEventName.RUNNING, this.parseRemainTime(this.remainTime), this.remainTime)
        // 判断如果还有剩余时间，则进行递归，继续调用当前函数；否则结束倒计时
        if (this.remainTime > 0) {
            // requsetAnimationFrame
            setTimeout(() => this.countdown(), this.step)
        } else {
            this.stop()
        }
    }

    private parseRemainTime = (remainTime: number): RemainTimeData =>{
        let time = remainTime
        // 计算出具体的时间格式
        const days = Math.floor(time / Countdown.DAY_IN_MILLISECOND)
        time = time % Countdown.DAY_IN_MILLISECOND

        const hours = Math.floor(time / Countdown.HOUR_IN_MILLISECOND)
        time = time % Countdown.HOUR_IN_MILLISECOND

        const minutes = Math.floor(time / Countdown.MINUTE_IN_MILLISECOND)
        time = time % Countdown.MINUTE_IN_MILLISECOND

        const seconds = Math.floor(time / Countdown.SECOND_IN_MILLISECOND)
        time = time % Countdown.SECOND_IN_MILLISECOND

        const count = Math.floor(time / Countdown.COUNT_IN_MILLISECOND)

        return {
            days,
            hours,
            minutes,
            seconds,
            count,
        };
    }
}

export const fillZero = (num: number) => { return `0${num}`.slice(-2) }
