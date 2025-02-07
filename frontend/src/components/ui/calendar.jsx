import React, { useEffect, useState } from "react";
import { Calendar } from "react-calendar";
import "react-calendar/dist/Calendar.css";
import moment from "moment";
import { FaCalendarAlt } from "react-icons/fa";  // react-icons에서 캘린더 아이콘 가져오기
import * as S from "./calendarStyle";

// 이유진
const CustomCalendar = ({ onChange, value }) => {
  const [nowDate, setNowDate] = useState("");
  const [isOpen, setIsOpen] = useState(false);

  const handleToggleCalendar = () => {
    setIsOpen(!isOpen);
  };

  const handleDateChange = (selectedDate) => {
    onChange(selectedDate);
    setIsOpen(false);
    setNowDate(moment(selectedDate).format("YYYY년 MM월 DD일"));
  };

  useEffect(() => {
    if (value) {
      setNowDate(moment(value).format("YYYY년 MM월 DD일"));
    } else {
      setNowDate("날짜");
    }
  }, [value]);
  

  return (
    <S.CalendarContainer>
      <S.DropdownButton selectedDate={nowDate !== "날짜"} onClick={handleToggleCalendar}>
        {nowDate}
        <S.CalendarIcon>
          <FaCalendarAlt />
        </S.CalendarIcon>
      </S.DropdownButton>
      <S.CalendarWrapper isOpen={isOpen}>
        <Calendar onChange={handleDateChange} value={value} />
      </S.CalendarWrapper>
    </S.CalendarContainer>
  );
};

export default CustomCalendar;
