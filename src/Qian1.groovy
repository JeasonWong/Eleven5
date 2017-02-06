import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.util.regex.Pattern

/**
 * Created by bazhang on 2017/2/6.
 */
class Qian1 {

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String RESET = "\u001B[0m";

    static void main(String[] args) {

        def map = [:]
        map['山东'] = 'http://chart.icaile.com/?op=dcr1&num=500'
        map['广东'] = 'http://gd11x5.icaile.com/?op=dcr1&num=500'
        map['江西'] = 'http://jx11x5.icaile.com/?op=dcr1&num=500'

        printDivider()

        map.each { key, value ->

            print "\t\t\t\t"

            for (int i = 1; i <= 11; i++) {
                print i + "\t"
            }

            println ""

            println key

            Document document = Jsoup.connect(value)
                    .maxBodySize(0)
                    .timeout(10000)
                    .get()

            Elements trs = document.getElementsByClass("chart-bg-foot").select("tr")

            Elements trsMax = document.getElementsMatchingText("历史最大遗漏").last().parent().parent().select("tr")

            trs.addAll(trsMax)

            for (int i = 0; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td")

                for (int j = 0; j < 13; j++) {
                    def text = tds.get(j).text()
                    if ("当前遗漏值".equals(tds.get(0).text()) && isNumeric(text)) {
                        if (Integer.parseInt(text) > 40) {
                            print GREEN + text + "\t" + RESET
                        } else if (Integer.parseInt(text) > 30) {
                            print BLUE + text + "\t" + RESET
                        } else {
                            print text + "\t"
                        }
                    } else {
                        print text + "\t"
                    }
                }

                println ""
            }

            printDivider()
        }

    }

    static void printDivider() {
        for (int i = 0; i < 68; i++) {
            print "="
        }
        println ""
    }

    static boolean isNumeric(String str) {
        if (str != null && !"".equals(str.trim())) {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        return false;
    }

}
