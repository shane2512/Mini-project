import psycopg2

# Connect to Supabase PostgreSQL
conn = psycopg2.connect(
    host="aws-1-ap-northeast-1.pooler.supabase.com",
    port=5432,
    database="postgres",
    user="postgres.ftbytspvsadvboiknmmy",
    password="Forex0809101112",
    sslmode="require"
)

cur = conn.cursor()

# First, let's see all users and their KYC status
print("=== All Users and KYC Status ===")
cur.execute("SELECT id, full_name, email, kyc_status, account_status FROM ftms_users;")
users = cur.fetchall()
for user in users:
    print(f"ID: {user[0]}, Name: {user[1]}, Email: {user[2]}, KYC: {user[3]}, Account: {user[4]}")

# Now approve KYC for all users
print("\n=== Approving KYC and Accounts ===")
cur.execute("UPDATE ftms_users SET kyc_status = 'APPROVED' WHERE kyc_status = 'PENDING';")
cur.execute("UPDATE ftms_users SET account_status = 'APPROVED' WHERE account_status = 'PENDING';")
conn.commit()

# Verify the update
print("\n=== Updated Status ===")
cur.execute("SELECT id, full_name, email, kyc_status, account_status FROM ftms_users;")
users = cur.fetchall()
for user in users:
    print(f"ID: {user[0]}, Name: {user[1]}, Email: {user[2]}, KYC: {user[3]}, Account: {user[4]}")

cur.close()
conn.close()
print("\n✅ All users are now approved!")
