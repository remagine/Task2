import Task.TaskService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final TaskService taskService = TaskService.getInstance();

    public static void main(String[] args) {
        // Task 처리와 관련된 실행문을 지역변수로 선언합니다.
        // 지역변수는 블록이 시작될 때 힙 메모리에 생성됩니다. 또 반드시 초기화를 해야 합니다.
        // 전역변수나 클래스 변수와 다르게 변수의 값을 할당할 시점을 제어할 수 없기 때문입니다
        // 자바 텍스트 블럭으로 선언하였습니다. 15버전부터 사용가능
        String input = """
                21
                create
                create
                create
                create
                execute 11
                create
                create
                create
                create
                create
                create
                execute 2
                create
                execute 2
                execute 11
                execute 2
                execute 5
                execute 5
                execute 2
                execute 5
                execute 5""";
        // 백준 알고리즘 문제를 풀때는 Scanner(System.in)을 통해 입력을 받는데 실제 채점을 할때는??? todo.
        // 컴퓨터가 데이터를 받기 위해서는 스트림을 통해서 받을 수 있는데
        // IO를 통해 컴퓨터가 데이터를 받기 위해서는 물리 데이터인 바이트가 필요하고
        // 때문에 메모리에 있는 문자열 데이터를 바이트로 변환합니다
        // 변환 규칙인 인코딩 타입 정보가 필요합니다.
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        // inputStream 처리에 집중하기 위해 ByteArrayInputStream을 업캐스팅하여 인풋스트림 객체를 만들고
        InputStream inputStream = new ByteArrayInputStream(bytes);
        // 통신비용을 줄이기 위해 버퍼처리가 내장된 보조스트림으로 인풋스트림을 감쌉니다.
        // 8192 byte 크기는 바이트 처리에서 사용되는 범용적인 크기이다.
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 8192);
        // 바이트 스트림은 물리데이터로 문자열로 처리하기 위해서는 인코딩처리가 필요하다
        // 물리 <> 논리 변환에는 인코딩 정보가 필요하며
        // 자바에서는 보조 스트림인 Reader/Writer로 감싸 처리한다.
        // 이렇게 인코딩정보를 전달받아 생성된 reader로 데이터를 다루게 되면
        // 문자열 스트림의 인코딩 정보가 보장되며, 일일이 변환할 필요도 없어 효율적이다
        InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8);
        // 문자열 스트림 처리도 버퍼처리된 보조 스트림으로 감싸 처리한다.
        // 여기서 size는 바이트가 아닌 문자열의 크기이다.
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);
        // IO처리에서 사용되는 스트림은 컴퓨터의 자원으로
        // 사용한 자원에 대한 해제 처리가 필수적이다.
        // 왜나하면 컴퓨터는 자원의 해제 여부를 알 수 없기 때문에 반드시 알려줘야 하고
        // 사용된 자원이 반납(해제) 되지 않으면 교착상태에 빠질 수 있다.
        // 교착 상태란 해제되지 않은 자원을 할당 받기 위해 대기하는 것을 말하며
        // 자원의 활용을 위한 부가적인 리소스들이 쌓이며 전체적인 성능을 저하시킨다.
        // 이런 처리를 관리하기 위한 용례로 with 구문이 있고
        // 자바에서는 try with resource 를 활용한다.
        // try() 안에 사용할 자원들을 넣어주면
        // 해당 자원의 해제와 예외를 관리해 줍니다.
        // 또한 보조 스트림을 닫으면 자동적으로 상위 스트림도 닫아줍니다.
        // try with resource로 자원을 관리 하기 위해서는 Closeable을 구현해야 합니다.
        // 그래야 해당 인터페이스를 통해 자원을 관리할 수 있습니다.
        try (inputStream; bufferedInputStream; inputStreamReader; bufferedReader) {
            List<String> lines = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
            int executeCnt = Integer.parseInt(lines.get(0));
            lines.remove(0);

            for (int i = 0; i < executeCnt; i++) {
                String line = lines.get(i);
                String[] commandArray = line.split(" ");
                String command = commandArray[0];
                if ("create".equals(command)) {
                    taskService.create();
                    continue;
                }

                if ("execute".equals(command)) {
                    int tag = Integer.parseInt(commandArray[1]);
                    taskService.execute(tag);
                }
            }

            taskService.printTaskResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
