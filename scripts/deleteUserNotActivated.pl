#!/usr/bin/perl
use strict;
use warnings;

use DBI;

print "Perl MySQL Delete User Not Activated";

my $db1 = "MBAL_server";
my $host = "localhost";
my $port = "3306";
my $user = "valentin";
my $pw = "Frigoule34";

my $dsn = "DBI:mysql:database=$db1;host=$host;port=$port";
my $dbh = DBI->connect($dsn, $user, $pw,
    { RaiseError => 1, AutoCommit => 1 });

print "Users deletion has started !";

# delete records from table
my $sql = "DELETE FROM user
WHERE creation_date < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 3 DAY))";
my $sth = $dbh->prepare($sql);
my $sth1 = $dbh->do($sql);

print "Users have been deleted !";

$dbh->disconnect;

exit;

