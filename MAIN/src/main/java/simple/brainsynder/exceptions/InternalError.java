/*
* MIT License
*
* Copyright (c) 2017 ramidzkh
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package simple.brainsynder.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

/**
* An internal error, made for uses with the Bukkit API.
* Extends {@link RuntimeException} for simplicity
*
* @author ramidzkh
*/
public class InternalError extends RuntimeException {

    private static final long serialVersionUID = -381479618743268720L;

    public InternalError() {
        super();
    }

    public InternalError(String message) {
        super(message);
    }

    public InternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalError(Throwable cause) {
        super(cause);
    }

    protected InternalError(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
   
    public void sendBukkit(CommandSender sender) {
        send((s) -> sender.sendMessage(ChatColor.RED + s));
    }
   
    public void send(Consumer<String> s) {
        String ie = "An internal error occured";
        if(getMessage() != null) ie += ": " + getMessage();
        s.accept(ie);
       
        for(StackTraceElement ele : getStackTrace())
            s.accept("  at " + ele.toString());
       
        for(Throwable thr : getSuppressed())
            printThr(s, thr);
    }
   
    private static void printThr(Consumer<String> s, Throwable thr) {
        s.accept("Caused by: " + thr.getClass().getName() +
                ((thr.getMessage() != null) ? ": " + thr.getMessage() : ""));
       
        for(StackTraceElement ele : thr.getStackTrace())
            s.accept("  at " + ele.toString());
    }
}