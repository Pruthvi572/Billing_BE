package com.billing.Invoizo.util;

public class CurrencyInWords {

    public static String currencyInWords(Double tempAmount) {
        System.out.println("Expecting Double - " + tempAmount);
        if (tempAmount == null || tempAmount == 0) {
            return "Zero";
        }
        return theConvertedMethodResult(tempAmount);

    }

    private static String theConvertedMethodResult(Double tempAmount) {

        String amount = String.format("%10f", tempAmount).trim();
        StringBuilder str2 = new StringBuilder();
        NumToWords w = new NumToWords();
        StringBuilder str1 = new StringBuilder();
        if (amount.contains(".")) {
            long rupees = Long.parseLong(amount.split("\\.")[0]);
            if (rupees > 999999999) {
                return null;
            }
            if (rupees < 0) {
                str1.append(" Minus ");
            }
            str1.append(w.convert(Math.abs(rupees)));
            str1.append(" Rupees ");
            long paise = Long.parseLong(amount.split("\\.")[1].substring(0, 2));
            if (paise != 0) {
                str2.append(" and ");
                str2.append(w.convert(paise));
                str2.append(" Paise");
            }
        } else {
            long rupees = Long.parseLong(amount);
            str1.append(w.convert(rupees));
            str1.append(" Rupees ");
        }
        return str1.append(str2).append(" Only").toString();
    }

    // public static String currencyInWords(String amount) {
    // if(amount)
    // System.out.println("Expecting Double - "+amount);
    // StringBuilder str2 = new StringBuilder();
    // NumToWords w = new NumToWords();
    // String str1 = null;
    // if (amount.contains(".")) {
    // long rupees = Long.parseLong(amount.split("\\.")[0]);
    // if (rupees > 999999999)
    // return null;
    // if(rupees < 0)
    // str1 = " Minus ";
    // str1 += w.convert(Math.abs(rupees));
    // str1 += " Rupees ";
    // int paise = Integer.parseInt(amount.split("\\.")[1]);
    // if (paise != 0) {
    // str2.append(" and ");
    // str2 = w.convert(paise);
    // str2 += " Paise";
    // }
    // } else {
    // long rupees = Long.parseLong(amount);
    // str1 = w.convert(rupees);
    // str1 += " Rupees ";
    // }
    // return str1 + str2 + " Only";
    // }

    // public static String currencyInWords(Long tempAmount) {
    // System.out.println("Expecting Long - "+tempAmount);
    // if (tempAmount == null || tempAmount == 0)
    // return "Zero";
    // String amount = tempAmount.toString();
    // String str2 = "";
    // NumToWords w = new NumToWords();
    // String str1 = null;
    // if (amount.contains(".")) {
    // long rupees = Long.parseLong(amount.split("\\.")[0]);
    // if (rupees > 999999999)
    // return null;
    // if(rupees < 0)
    // str1 = " Minus ";
    // str1 += w.convert(Math.abs(rupees));
    // str1 += " Rupees ";
    // long paise = Long.parseLong(amount.split("\\.")[1]);
    // if (paise != 0) {
    // str2 += " and";
    // str2 = w.convert(paise);
    // str2 += " Paise";
    // }
    // } else {
    // long rupees = Long.parseLong(amount);
    // str1 = w.convert(rupees);
    // str1 += " Rupees ";
    // }
    // return str1 + str2 + " Only";
    // }

    public static String percentageInWords(String percentage) {
        String str2 = "";
        NumToWords w = new NumToWords();
        String str1 = null;
        if (percentage.contains(".")) {
            long per1 = Long.parseLong(percentage.split("\\.")[0]);
            long per2 = Integer.parseInt(percentage.split("\\.")[1]);
            if (per1 > 999999999) {
                return null;
            }
            if (per1 == 0) {
                str1 = "Zero point ";
            } else {
                if (per2 != 0) {
                    str1 = w.convert(per1);
                    str1 += " point ";
                } else {
                    str1 = w.convert(per1);
                    str1 += " ";
                }
            }
            if (per2 != 00) {
                str2 += " ";
                str2 = w.convert(per2);
            }
        } else {
            long per1 = Long.parseLong(percentage);
            str1 = w.convert(per1);
            str1 += "  ";
        }
        return str1 + str2 + " Percentage";
    }
}


class NumToWords {

    String string;
    String[] st1 = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    String[] st2 = {"Hundred", "Thousand", "Lakh", "Crore"};
    String[] st3 = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen"};
    String[] st4 = {"Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    public String convert(long number) {
        int n = 1;
        long word;
        string = "";
        while (number != 0) {
            switch (n) {
                case 1:
                    word = number % 100;
                    pass(word);
                    if (number > 100 && number % 100 != 0) {
                        show("and ");
                    }
                    number /= 100;
                    break;
                case 2:
                    word = number % 10;
                    if (word != 0) {
                        show(" ");
                        show(st2[0]);
                        show(" ");
                        pass(word);
                    }
                    number /= 10;
                    break;
                case 3:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(st2[1]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
                case 4:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(st2[2]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
                case 5:
                    word = number % 100;
                    if (word != 0) {
                        show(" ");
                        show(st2[3]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
                default:
                    break;
            }
            n++;
        }
        return string;
    }

    public void pass(long number) {
        long word, q;
        if (number < 10) {
            show(st1[(int) number]);
        }
        if (number > 9 && number < 20) {
            show(st3[(int) (number - 10)]);
        }
        if (number > 19) {
            word = number % 10;
            if (word == 0) {
                q = number / 10;
                show(st4[(int) (q - 2)]);
            } else {
                q = number / 10;
                show(st1[(int) word]);
                show(" ");
                show(st4[(int) (q - 2)]);
            }
        }
    }

    public void show(String s) {
        String st;
        st = string;
        string = s;
        string += st;
    }
}
