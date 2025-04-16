import CommandsProvider.Flat;
import java.util.Scanner;

public class ByFlat {
    public void FlatMethod(Flat fl){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите имя: ");
        fl.setName(scanner.nextLine()); // исправлено на setName

        while (true){
            try{
                System.out.print("Введите id: ");
                int id = Integer.parseInt(scanner.nextLine());
                if (id<0){
                    System.out.println("ID должен быть больше нуля");
                }else{
                    fl.setId(id); // исправлено на setId
                    break;
                }

            } catch (NumberFormatException e){
                System.out.println("Тип данных должен быть int");
            }
        }
    }
}