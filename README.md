# EmployeeManager
Trang web quản lý nhân viên gồm các chức năng sau:
- Đăng ký tài khoản 
- Login/Logout (Login bằng username/password, Login qua tài khoản google)
- Phân Quyền Admin và Employee
- Checkin/checkout bằng mã nhân viên


Admin:
- Thêm/sửa/xóa nhân viên, khi thêm nhân viên mới tạo mã checkin là một số random gồm 4 chữ số cho mỗi nhân viên
- Tìm kiếm/sắp xếp nhân viên theo tên
- Hiển thị danh sách tất cả nhân viên kèm thông tin giờ checkin/checkout của từng ngày trong khoảng thời gian lựa chọn (default là tuần hiện tại)
- Hiển thị danh sách lỗi checkin của các nhân viên trong tháng (checkin muộn hoặc không checkin)
- Approve/Reject request nghỉ,thay đổi giờ làm


Employee:
- Sử dụng mã số đã được cấp để checkin/checkout
- Hiển thị danh sách checkin/checkout của riêng nhân viên đó trong khoảng thời gian lựa chọn(default là tuần hiện tại) 
- Hiển thị lỗi checkin của riêng nhân viên đó trong tháng
- Request nghỉ sáng/chiều, thay đổi giờ làm


Note:
- Gửi mail cho employee khi employee được create.
- Cronjob gửi mail thông báo quên checkin/checkout hàng ngày.
- Cronjob gửi mail tổng hợp checkin/checkout và lỗi phạt mỗi tuần cho từng nhân viên. 
