package com.p1neapplexpress.telegrec;

interface IRecordingSaver {
    void saveRecording(in String name, in byte[] buf);
}