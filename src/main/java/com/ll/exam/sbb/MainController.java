package com.ll.exam.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;



@Controller
public class MainController {
    private int increaseNo =0; // 지역변수는 함수실행 후 소멸 -> 클래스변수로 선언해줘야 increase() 새로고침할 때마다 ++ 증가

    @RequestMapping("/sbb")
    // 아래 함수의 리턴값을 그대로 브라우저에 표시
    // 아래 함수의 리턴값을 문자열화 해서 브라우저 응답의 바디에 담는다.
    @ResponseBody
    public String index() {
        // 서버에서 출력
        System.out.println("Hello");
        // 먼 미래에 브라우저에서 보여짐
        return "안녕하세요!";
    }

    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type="number" name="age" placeholder="나이" />
                    <input type="submit" value="page2로 POST 방식으로 이동" />
                </form>
                """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/plus")
    @ResponseBody
    public int plus (int a ,int b){ // 리턴타입 String 아니고 int도 ok. java에서 browser로 전송할 때 어차피 문장화됨.
        return a+b;
    }

    @GetMapping("/minus")
    @ResponseBody
    public int minus (int a ,int b){
        return a-b;
    }

    @GetMapping("/increase")
    @ResponseBody
    public int increase (){
        increaseNo++;
        return increaseNo;

    }

    @GetMapping("/gugudan")
    @ResponseBody
    public String gugudan(Integer dan, Integer limit) { // Integer는 null 가능. int는 null 불가.
    Integer finalDan = dan;
        return IntStream.rangeClosed(1, limit)
            .mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
            .collect(Collectors.joining("<br>\n"));
    }
}