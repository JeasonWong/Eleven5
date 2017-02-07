import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.regex.Pattern

/**
 * Created by bazhang on 2017/2/6.
 */
class Qian1 {

    //爬取数据量,100 200 500 1000
    static final def MAX_NUM = 500;

    static final def BLACK = "\u001B[30m";
    static final def RED = "\u001B[31m";
    static final def GREEN = "\u001B[32m";
    static final def YELLOW = "\u001B[33m";
    static final def BLUE = "\u001B[34m";
    static final def PURPLE = "\u001B[35m";
    static final def CYAN = "\u001B[36m";
    static final def WHITE = "\u001B[37m";
    static final def RESET = "\u001B[0m";

    static void main(String[] args) {

//        MailManager.sendMail("22")

        createOmit()

    }

    static def createOmit() {

        StringBuilder sb = new StringBuilder()
        StringBuilder sbContent = new StringBuilder()

        def urls = [:]
        urls['山东'] = 'http://chart.icaile.com/?op=dcr1&num=' + MAX_NUM
        urls['广东'] = 'http://gd11x5.icaile.com/?op=dcr1&num=' + MAX_NUM
        urls['江西'] = 'http://jx11x5.icaile.com/?op=dcr1&num=' + MAX_NUM

        sb.append(createDivider())

        while (true) {
            urls.each { key, value ->

                Document document = Jsoup.connect(value)
                        .maxBodySize(0)
                        .timeout(10000)
                        .get()

                Element no = document.getElementsByClass("second-num-list alignRight").select("li").last()

                sb.append("(").append(no.text()).append("\n")

                sb.append("\t\t\t\t")

                for (int i = 1; i <= 11; i++) {
                    sb.append(i + "\t")
                }

                sb.append("\n")

                sb.append(key).append("\n")

                Elements trs = document.getElementsByClass("chart-bg-foot").select("tr")

                Elements trsMax = document.getElementsMatchingText("历史最大遗漏").last().parent().parent().select("tr")

                trs.addAll(trsMax)

                for (int i = 0; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("td")

                    for (int j = 0; j < 13; j++) {
                        def text = tds.get(j).text()
                        if ("当前遗漏值".equals(tds.get(0).text()) && isNumeric(text)) {
                            if (Integer.parseInt(text) > 40) {
                                sbContent.append(key).append(" => ").append(j - 1).append("已经有").append(text).append("次没出现了!")
                                sb.append(GREEN + text + "\t" + RESET)
                            } else if (Integer.parseInt(text) > 30) {
                                sbContent.append(key).append(" => ").append(j - 1).append("已经有").append(text).append("次没出现了!")
                                sb.append(BLUE + text + "\t" + RESET)
                            } else {
                                sb.append(text + "\t")
                            }
                        } else {
                            sb.append(text + "\t")
                        }
                    }

                    sb.append("\n")
                }

                sb.append(createDivider())
            }

            if (Constants.SEND_MAIL && sbContent.size() > 0) {
                MailManager.sendMail("新机会来了!", sbContent.toString())
            }

            print sb.toString()

            //清空
            sb.setLength(0)
            sbContent.setLength(0)

            sleep(60000 * 3)
        }
    }

    static def createDivider() {
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < 68; i++) {
            sb.append("=")
        }
        return sb.append("\n").toString()
    }

    static def isNumeric(String str) {
        if (str != null && !"".equals(str.trim())) {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        return false;
    }

}
