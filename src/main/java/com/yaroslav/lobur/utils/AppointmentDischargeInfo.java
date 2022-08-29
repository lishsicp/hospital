package com.yaroslav.lobur.utils;

import com.yaroslav.lobur.model.entity.Appointment;
import com.yaroslav.lobur.model.entity.Doctor;
import com.yaroslav.lobur.model.entity.Patient;
import com.yaroslav.lobur.model.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.ColumnLayout;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.text.BaseFont;
import java.io.*;
import java.util.Objects;


public class AppointmentDischargeInfo {
    public static void createDischargeInfo(Appointment appointment, HttpServletResponse response) throws IOException {
        Document document = new Document();

        PDFont regular = PDType0Font.load(document.getPDDocument(), new FileInputStream(Objects.requireNonNull(AppointmentDischargeInfo.class.getClassLoader().getResource("font/times.ttf")).getFile()));
        PDFont bold = PDType0Font.load(document.getPDDocument(), new FileInputStream(Objects.requireNonNull(AppointmentDischargeInfo.class.getClassLoader().getResource("font/timesbd.ttf")).getFile()));
        PDFont italic = PDType0Font.load(document.getPDDocument(), new FileInputStream(Objects.requireNonNull(AppointmentDischargeInfo.class.getClassLoader().getResource("font/timesi.ttf")).getFile()));

        Paragraph title = new Paragraph();
        title.addMarkup("*Patient Discharge Info*",
                20, BaseFont.Helvetica);
        title.setLineSpacing(2f);
        document.add(title, VerticalLayoutHint.CENTER);
        document.add(new VerticalSpacer(10));

        document.add(new ColumnLayout(2, 20), VerticalLayoutHint.CENTER);

        Patient patient = appointment.getHospitalCard().getPatient();
        Doctor doctor = patient.getDoctor();
        User executor = appointment.getUser();

        Paragraph paragraph1 = new Paragraph();
        paragraph1.setLineSpacing(1.5f);
        paragraph1.addText("Patient Details\n", 16, italic);
        paragraph1.addText(String.format("Name: %s %s%n", patient.getFirstname(), patient.getLastname()), 14, regular);
        paragraph1.addText(String.format("Email: %s%n", patient.getEmail()), 14, regular);
        paragraph1.addText(String.format("Date of birth: %s%n", patient.getDateOfBirth().toString()), 14, regular);
        paragraph1.addText(String.format("Gender: %s%n", patient.getGender().toLowerCase()), 14, regular);
        paragraph1.addText("\n", 14, regular);
        paragraph1.addText("\n", 14, regular);
        paragraph1.addText("Patient's doctor\n", 16, italic);
        paragraph1.addText(String.format("Name: %s %s%n", doctor.getUser().getFirstname(), doctor.getUser().getLastname()), 14, regular);
        paragraph1.addText(String.format("Email: %s%n", doctor.getUser().getEmail()), 14, regular);
        paragraph1.addText(String.format("Phone: %s%n", doctor.getUser().getPhone()), 14, regular);

        document.add(paragraph1, VerticalLayoutHint.CENTER);
        document.add(ColumnLayout.NEWCOLUMN);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.setLineSpacing(1.5f);
        paragraph2.addText("Treatment Details\n", 16, italic);
        paragraph2.addText(String.format("Description: %s%n", appointment.getTitle()), 14, regular);
        paragraph2.addText(String.format("Type: %s%n", appointment.getType().name().toLowerCase()), 14, regular);
        paragraph2.addText(String.format("Treatment started: %s%n", appointment.getStartDate().toString()), 14, regular);
        paragraph2.addText(String.format("Treatment ended: %s%n", appointment.getEndDate().toString()), 14, regular);
        paragraph2.addText(String.format("Appointment made by : %s %s %s%n", executor.getFirstname(), executor.getLastname(), executor.getRole().name().toLowerCase()), 14, regular);
        paragraph2.addText("\n", 14, regular);
        paragraph2.addText(String.format("Diagnosis: %s%n", appointment.getHospitalCard().getDiagnosis()), 18, bold);
        document.add(paragraph2, VerticalLayoutHint.CENTER);
        document.save(response.getOutputStream());
    }
}
