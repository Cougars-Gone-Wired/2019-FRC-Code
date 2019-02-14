package frc.robot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {

    @Override
    public String format(LogRecord line) {
        StringBuilder sb = new StringBuilder();
        String timeStamp = new SimpleDateFormat("MMM dd, YYYY_HH.mm.ss.SSS").format(Calendar.getInstance().getTime());
        //sb.append(timeStamp);
        sb.append(line.getMessage());
        sb.append("\n");
        return sb.toString();   
    }
    
}