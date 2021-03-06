package com.sistmng.instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sistmng.Current;
import com.sistmng.SQLConnection;

public class InstructorDAO {


	public List<Instructor> instructorInfo() {

		/*
		 * CREATE OR REPLACE VIEW InstructorInfoView AS SELECT m.name_ , m.phone , m.ssn
		 * , i.instructorRegDate , c.subjectCode , s.subjectName FROM member_ m ,
		 * instructor_ i, checkSubject_ c , subject_ s WHERE m.mid = i.mid AND i.mid =
		 * c.mid AND s.subjectCode = c.subjectCode;
		 */

		List<Instructor> instructorInfo = new ArrayList<>();

		// 강사명 / 주민번호 / 전화번호 / 강의 가능 목록

		String sql = "SELECT name_, phone, ssn, instructorRegDate, subjectCode, subjectName FROM InstructorInfoView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Current.getInstance().getCurrent());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				// 강사 이름
				i.setName_(rs.getString("name_"));

				// 강사 주민번호
				i.setSsn(rs.getString("ssn"));

				// 강사 전화번호
				i.setPhone(rs.getString("phone"));

				// 강의 가능 목록 과목 코드, 과목명
				i.setSubjectCode(rs.getString("subjectCode"));
				i.setSubjectName(rs.getString("subjectName"));

				// 강사 등록일
				i.setInstructorRegDate(rs.getDate("instructorRegDate").toLocalDate());

				instructorInfo.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return instructorInfo;
	}

	public List<Instructor> subjectListByInstructor() {

		List<Instructor> subjectListByInstructor = new ArrayList<Instructor>();

		/*
		 * CREATE OR REPLACE VIEW subjectListByInstructorView AS SELECT
		 * openS.openSubCode , subj.subjectName , openS.openSubStartDate ,
		 * openS.openSubCloseDate , books.bookName , openC.openCoCode , cours.courseName
		 * , openC.openCoStartDate , openC.openCoCloseDate ,
		 * clas.className,COUNT(*)OVER(PARTITION BY subj.subjectName) AS
		 * number_of_student, mem.mid, mem.name_ FROM openSubject_ openS , subject_ subj
		 * , books_ books , course_ cours , openCourse_ openC , class_ clas ,
		 * instructor_ instr , member_ mem WHERE openS.subjectCode = subj.subjectCode
		 * AND openS.openCoCode = openC.openCoCode AND openS.bookCode = books.bookCode
		 * AND openC.courseCode = cours.courseCode AND openC.classCode = clas.classCode
		 * AND openS.mid = instr.mid AND mem.mid = instr.mid GROUP BY openS.openSubCode
		 * , subj.subjectName , openS.openSubStartDate , openS.openSubCloseDate ,
		 * books.bookName , openC.openCoCode , cours.courseName , openC.openCoStartDate
		 * , openC.openCoCloseDate , clas.className, mem.mid, mem.name_ ORDER BY
		 * openS.openSubStartDate DESC;
		 */

		// 과목코드 / 과목명 / 과목 시작일 / 과목 종료일 / 교재명/ 과정코드 / 과정명 / 과정 시작일/ 과정 종료일 / 강의실 / 등록인원 / 상태

		String sql = "SELECT openSubCode , subjectName , openSubStartDate , openSubCloseDate , bookName , openCoCode , courseName , openCoStartDate , openCoCloseDate , className, number_of_student FROM subjectListByInstructorView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Current.getInstance().getCurrent());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());
				i.setBookName(rs.getString("bookName"));
				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));
				i.setNumberOfStuHaveScore(rs.getInt("number_of_student"));

				subjectListByInstructor.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListByInstructor;

	}

	public Instructor selectedSubjectByInstructor(String subCode) {
		Instructor i = new Instructor();

		/*
		 * CREATE OR REPLACE VIEW selectedSubjectByInstructor AS SELECT openSubCode ,
		 * subjectName , openSubStartDate , openSubCloseDate FROM subject_ sub ,
		 * openSubject_ openS , openCourse_ openC WHERE sub.subjectCode =
		 * openS.subjectCode GROUP BY openSubCode , subjectName , openSubStartDate ,
		 * openSubCloseDate ORDER BY openSubCode ASC;
		 */

		// 과목코드 / 과목명 / 과목시작일 / 과목종료일
		String sql = "SELECT openSubCode, subjectName, openSubStartDate, openSubCloseDate FROM selectedSubjectByInstructor WHERE openSubCode = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, subCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return i;
	}

	public List<Instructor> studentListBySubject(String subCode) {

		/*
		 * CREATE OR REPLACE VIEW studentListBySubjectView AS SELECT m.mid, m.name_
		 * ,m.phone, s.sstudentRegDate, openS.openSubCode ,openS.openSubStartDate ,
		 * openS.openSubCloseDate, dropO.failureCode, dropO.failureDate FROM member_ m ,
		 * student_ s, studentHistory_ sh , openCourse_ openC , openSubject_ openS ,
		 * dropOut_ dropO WHERE m.mid = s.mid AND s.mid = sh.mid AND openC.openCoCode =
		 * sh.openCoCode AND openC.openCoCode = openS.openCoCode AND sh.openCoCode =
		 * dropO.openCoCode(+) AND sh.mid = dropO.mid(+) GROUP BY m.mid, m.name_
		 * ,m.phone, s.sstudentRegDate, openS.openSubCode ,openS.openSubStartDate ,
		 * openS.openSubCloseDate, dropO.failureCode ,dropO.failureDate;
		 */

		List<Instructor> studentListBySubject = new ArrayList<Instructor>();

		// 회원코드 / 이름 / 전화번호 / 등록일 / 개설과목시작일 / 개설과목종료일 / 탈락코드 / 탈락 날짜 / 수료
		String sql = "SELECT mid, name_, phone, sstudentRegDate, openSubStartDate, openSubCloseDate, NVL(failureCode, 'noFailureCode') finalFailureCode, NVL(failureDate, '1901-01-01') finalFailureDate FROM studentListBySubjectView	 Where openSubCode = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, subCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setMid(rs.getString("mid"));
				i.setName_(rs.getString("name_"));
				i.setPhone(rs.getString("phone"));

				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

				i.setStudentRegDate(rs.getDate("sstudentRegDate").toLocalDate());

				// 중도탈락코드 null이면 "noFailureCode"로 리턴
				i.setStudentStatus(rs.getString("finalFailureCode"));

				// 중도탈락날짜가 null이면
				i.setFailureDate(rs.getDate("finalFailureDate").toLocalDate());

				studentListBySubject.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return studentListBySubject;
	}

	public List<Instructor> subjectListWithTestInfo() {

		List<Instructor> subjectListWithTestInfo = new ArrayList<Instructor>();

		/*
		 * CREATE OR REPLACE VIEW subjectListWithTestInfoView AS SELECT inst.mid,
		 * openS.openSubCode , subj.subjectName , openS.openSubStartDate ,
		 * openS.openSubCloseDate , books.bookName , openC.openCoCode ,
		 * course.courseName , openC.openCoStartDate , openC.openCoCloseDate ,
		 * clas.className ,tes.testCode,tes.testDate, dist.attDistribution ,
		 * dist.wriDistribution , dist.pracDistribution, tes.testFile FROM subject_ subj
		 * , openSubject_ openS , books_ books , course_ course , openCourse_ openC ,
		 * class_ clas , distribution_ dist , test_ tes , instructor_ inst WHERE
		 * subj.subjectCode = openS.subjectCode AND books.bookCode = openS.bookCode AND
		 * course.courseCode = openC.courseCode AND clas.classCode = openC.classCode AND
		 * openC.openCoCode = openS.openCoCode AND dist.testCode(+) = tes.testCode AND
		 * tes.openSubCode = openS.openSubCode AND inst.mid = openS.mid AND
		 * openS.openSubCloseDate < TO_CHAR(SYSDATE,'YYYY-MM-DD');
		 */

		// 과목코드 / 과목명 / 과목시작일 / 과목종료일 / 교재명 / 과정코드 / 과정명 / 과정시작일 / 과정종료일 / 강의실 / 시험날짜 /
		// 출결배점 / 필기배점 / 실기배점 / 시험문제
		String sql = "SELECT openSubCode, subjectName, openSubStartDate, openSubCloseDate, bookName, openCoCode, courseName, openCoStartDate, openCoCloseDate, className, testCode, testDate, NVL(attDistribution, 0) finalAttDistribution, NVL(wriDistribution, 0) finalWriDistribution, NVL(pracDistribution, 0) finalPracDistribution, NVL(testFile, 'no file') finalTestFile FROM subjectListWithTestInfoView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Current.getInstance().getCurrent());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

				i.setBookName(rs.getString("bookName"));

				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));
				i.setTestCode(rs.getString("testCode"));
				i.setTestDate(rs.getDate("testDate").toLocalDate());

				i.setAttendanceDistribution(rs.getInt("finalAttDistribution"));

				i.setWritingDistribution(rs.getInt("finalWriDistribution"));

				i.setPracticeDistribution(rs.getInt("finalPracDistribution"));

				i.setTestFile(rs.getString("finalTestFile"));

				subjectListWithTestInfo.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListWithTestInfo;
	}
	//배점 등록
	public int addDistribution(Instructor i) {

		int result1 = 0;
		int result2 = 0;

		String sql0 = "SELECT NVL(LPAD(SUBSTR(MAX(testCode),4,3)+1, 6, 'TES000'), 'TES001') finalTestCode FROM test_";

		String sql1 = "INSERT INTO test_ (testCode,openSubCode,testDate,testFile) VALUES (?,?,?,?)";

		String sql2 = "INSERT INTO distribution_ (disCode , testCode , attDistribution , wriDistribution , pracDistribution) VALUES ((SELECT NVL(LPAD(SUBSTR(MAX(disCode),4,3)+1, 6, 'DIV000'), 'DIV001') FROM distribution_), ?,?,?,?)";

		Connection conn = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String testCode = "";

		try {
			conn = SQLConnection.connect();

			// 1번으로 추가한것
			conn.setAutoCommit(false);

			pstmt0 = conn.prepareStatement(sql0);

			ResultSet rs = pstmt0.executeQuery();

			while (rs.next()) {
				testCode = rs.getString("finalTestCode");
			}
			rs.close();

			pstmt1 = conn.prepareStatement(sql1);

			pstmt1.setString(1, testCode);
			pstmt1.setString(2, i.getOpenSubCode());
			pstmt1.setString(3, i.getTestDate().toString());
			pstmt1.setString(4, i.getTestFile());

			result1 = pstmt1.executeUpdate();

			pstmt2 = conn.prepareStatement(sql2);

			pstmt2.setString(1, testCode);
			pstmt2.setInt(2, i.getAttendanceDistribution());
			pstmt2.setInt(3, i.getWritingDistribution());
			pstmt2.setInt(4, i.getPracticeDistribution());

		
			result2 = pstmt2.executeUpdate();

			conn.commit();

		} catch (SQLException se) {
			se.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt1 != null)
					pstmt1.close();

			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return result1 + result2;
	}

	//시험 정보 삭제
	public int deleteTest(String testCode) {

		int result = 0;

		String sql = "DELETE test_ WHERE testCode = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, testCode);

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {
			// System.out.println(e.getMessage());

			System.out.println();
			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {

			}

			try {
				SQLConnection.close();
			} catch (SQLException se) {
				System.out.println();
				System.out.println(se.getMessage());
			}
		}
		return result;
	}

	public List<Instructor> subjectListByNumberOfStudent() {

		List<Instructor> subjectListByNumberOfStudent = new ArrayList<>();

		// 과목코드 / 과목명 / 과목 시작일 / 과목 종료일 / 교재명/ 과정코드 / 과정명 / 과정 시작일 / 과정 종료일 / 강의실 / 등록인원
		// 상태

		String sql = "";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Current.getInstance().getCurrent());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());
				i.setBookName(rs.getString("bookName"));
				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));
				i.setNumberOfStuHaveScore(rs.getInt("number_of_student"));

				subjectListByNumberOfStudent.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListByNumberOfStudent;

	}

	
	public List<Instructor> scoreInfoByStudents(String openSubCode) {
		List<Instructor> scoreInfoByStudents = new ArrayList<>();

		// 회원코드 / 이름 / 전화번호 / 등록일 / 수료 / 출결점수 / 필기점수 / 실기점수 / 총점"

		String sql = "";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, openSubCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setMid(rs.getString("mid"));
				i.setName_(rs.getString("name_"));
				i.setPhone(rs.getString("phone"));
				i.setStudentRegDate(rs.getDate("studentRegDate").toLocalDate());

				// 중도탈락코드 null이면 "noFailureCode"로 리턴
				i.setStudentStatus(rs.getString("finalFailureCode"));

				i.setAttendanceScore(rs.getInt("attendanceScrore"));
				i.setWritingScore(rs.getInt("wrtingScore"));
				i.setPracticeScore(rs.getInt("practiceScore"));
				i.setTotalScore(rs.getInt("totalScore"));

				scoreInfoByStudents.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return scoreInfoByStudents;
	}

	//성적 추가
	public int addScore(Instructor i) {

		int result = 0;

		String sql = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, i.getAttendanceScore());
			pstmt.setInt(2, i.getWritingScore());
			pstmt.setInt(3, i.getPracticeScore());

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				// System.out.println(se.getMessage());
				System.out.println();
				System.out.println(se.getMessage());
			}
		}

		return result;
	}

	//성적 삭제
	public int deleteScore(String mid) {
		int result = 0;

		String sql = "";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mid);

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				// System.out.println(se.getMessage());
				System.out.println();
				System.out.println(se.getMessage());
			}
		}

		return result;
	}

}
