package com.p1neapplexpress.telegrec;

import com.p1neapplexpress.telegrec.data.Recording;

interface IRecordingSaver {
    void writeRecording(in String name, in byte[] buf);
    void saveRecording(in Recording recording);
    String getRecordingFormat();
    String getFileNameMask();
}